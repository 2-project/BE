package com.github.backendpart.config.security;

import com.github.backendpart.service.auth.UsersService;
import com.github.backendpart.web.dto.users.TokenDto;
import com.github.backendpart.web.entity.enums.Roles;
import com.github.backendpart.web.entity.users.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key encodeKey;
    private static final String AUTHORITIES_KEY = "roles";
    private static final String BEARER_TYPE = "Bearer";
    private static final Long ACCESS_TOKEN_EXPIRED_TIME = 60 * 60 * 1000L;   //60분
    private static final Long REFRESH_TOKEN_EXPIRED_TIME = 7 * 24 * 60 * 60 * 1000L;  //7일

    public JwtTokenProvider(@Value("${spring.jwt.secret}")String secretKey){
      byte[] keyBytes = Base64.getDecoder().decode(secretKey);
      this.encodeKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // accessToken과 refreshToken을 생성함
    // @param subject
    // @return TokenDto
    public TokenDto createTokenDto(Authentication authentication){
        //권한을 하나의 String으로 합침
        String roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        //토큰 생성시간
        Instant now = Instant.from(OffsetDateTime.now());

        //accessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, roles)
                .setExpiration(Date.from(now.plusMillis(ACCESS_TOKEN_EXPIRED_TIME)))
                .signWith(encodeKey, SignatureAlgorithm.HS256)
                .compact();

        //refreshToken 생성
        String refreshToken = Jwts.builder()
                .setExpiration(Date.from(now.plusMillis(REFRESH_TOKEN_EXPIRED_TIME)))
                .signWith(encodeKey, SignatureAlgorithm.HS256)
                .compact();

        //TokenDTO에 두 토큰을 담아서 반환
        return TokenDto.builder()
                .tokenType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .duration(Duration.ofMillis(ACCESS_TOKEN_EXPIRED_TIME))
                .build();
    }

    // UsernamePasswordAuthenticationToken으로 보내 인증된 유저인지 확인
    //     * @param accessToken
    //     * @return Authentication
    //     * @throws ExpiredJwtException
    public Authentication getAuthentication(String accessToken) throws ExpiredJwtException{
        // 토큰 복호화 : JWT의 body
        Claims claims = parseClaims(accessToken);

        if(claims.get(AUTHORITIES_KEY) == null) {
          throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(),"", authorities);
        return new UsernamePasswordAuthenticationToken(principal,"",authorities);
    }

    public Authentication checkRefreshToken(String refreshToken) throws ExpiredJwtException {
      Claims claims = Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(refreshToken).getBody();

      UserDetails user = new User(claims.getSubject(), "", null);
      return new UsernamePasswordAuthenticationToken(user, "", null);
    }

    public boolean validationToken(String tokenKey){
        try {
          Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(tokenKey).getBody();
          return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
          log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
          log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
          log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
          log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
      try{
        return Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(accessToken).getBody();
      }catch (ExpiredJwtException e) {
        return e.getClaims();
      }
    }
}
