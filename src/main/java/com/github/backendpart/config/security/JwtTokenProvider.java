package com.github.backendpart.config.security;

import com.github.backendpart.web.dto.users.TokenDto;
import com.github.backendpart.web.entity.enums.Roles;
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
    private static final String BEARER_TYPE = "Bearer";
    private static final Long accessTokenTime = 30 * 60 * 1000L;   //30분
    private static final Long refreshTokenTime = 7 * 24 * 60 * 60 * 1000L;  //7일

    public JwtTokenProvider(@Value("${spring.jwt.secret}")String secretKey){
      log.info("Secret Key: {}", secretKey);
      byte[] keyBytes = Base64.getDecoder().decode(secretKey);
      this.encodeKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // accessToken과 refreshToken을 생성함
    // @param subject
    // @return TokenDto
    public TokenDto createTokenDto(String userId, Roles roles){
        //권한을 하나의 String으로 합침
        String authority = roles.getType().toString();

        //토큰 생성시간
        Instant now = Instant.from(OffsetDateTime.now());

        //accessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(userId)
                .claim("roles", authority)
                .setExpiration(Date.from(now.plusMillis(accessTokenTime)))
                .signWith(encodeKey)
                .compact();

        //refreshToken 생성
        String refreshToken = Jwts.builder()
                .setExpiration(Date.from(now.plusMillis(refreshTokenTime)))
                .signWith(encodeKey)
                .compact();

        //TokenDTO에 두 토큰을 담아서 반환
        return TokenDto.builder()
                .tokenType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .duration(Duration.ofMillis(refreshTokenTime))
                .build();
    }

    // UsernamePasswordAuthenticationToken으로 보내 인증된 유저인지 확인
    //     * @param accessToken
    //     * @return Authentication
    //     * @throws ExpiredJwtException
    public Authentication getAuthentication(String accessToken) throws ExpiredJwtException{
        Claims claims = Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(accessToken).getBody();
        if(claims.get("roles") == null)
          throw new RuntimeException("권한 정보가 없는 토큰입니다.");

      Collection<? extends GrantedAuthority> roles =
              Arrays.stream(claims.get("roles").toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
      UserDetails user = new User(claims.getSubject(), "", roles);
      return new UsernamePasswordAuthenticationToken(user, "", roles);
    }

    public Authentication checkRefreshToken(String refreshToken) throws ExpiredJwtException {
      Claims claims = Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(refreshToken).getBody();

      UserDetails user = new User(claims.getSubject(), "", null);
      return new UsernamePasswordAuthenticationToken(user, "", null);
    }

    public boolean tokenMatches(String accessToken, String refreshToken) {
      Claims accessTokenClaim = Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(accessToken).getBody();
      Claims refreshTokenClaim = Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(refreshToken).getBody();

      if(accessTokenClaim.getSubject().equals(refreshTokenClaim.getSubject()))
        return true;

      return false;
    }


    public boolean validationToken(String tokenKey){
        try {
          //access token
          Claims claims = Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(tokenKey).getBody();
          if (claims.containsKey("role")) {
            return true;
          }

          //refresh token
          return false;
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
}
