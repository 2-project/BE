package com.github.backendpart.service.auth;

import com.github.backendpart.config.security.JwtTokenProvider;
import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.repository.RefreshTokenRepository;
import com.github.backendpart.web.dto.users.TokenDto;
import com.github.backendpart.web.dto.users.UserDto;
import com.github.backendpart.web.entity.users.RefreshTokenEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthRepository authRepository;


    // 소셜 로그인 시 MemberDTO를 받아와서 토큰을 생성하는 기능
    // 구현 안할 예정
    public TokenDto creatToken(UserDto userDto){
      TokenDto tokenDto = tokenProvider.createTokenDto(userDto.getUserId(), userDto.getRoles());
      UserEntity users = authRepository.findByUserId(userDto.getUserId()).orElseThrow(() -> new RuntimeException("잘못된 접근입니다.(사용자가 존재하지 않습니다.)"));
      RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
              .users(users)
              .token(tokenDto.getRefreshToken())
              .build();

      refreshTokenRepository.save(refreshToken);

      return tokenDto;
    }

    //createToken(users): 일반 로그인 시 Member를 받아와서 토큰을 생성하는 기능
    public TokenDto createToken(UserEntity users) {
      TokenDto tokenDto = tokenProvider.createTokenDto(users.getUserId(), users.getRoles());
      RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
              .users(users)
              .token(tokenDto.getRefreshToken())
              .build();

      refreshTokenRepository.save(refreshToken);

      return tokenDto;
    }

    // 클라이언트가 보낸 accessToken이 만료되었을 때 refreshToken을 이용하여 갱신할 수 있도록 하는 기능
    public TokenDto refresh(TokenDto tokenDto) {
      if(!tokenProvider.validationToken(tokenDto.getRefreshToken())) {
        throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
      }

      Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());

      RefreshTokenEntity refreshToken = refreshTokenRepository.findByUsers(authRepository.findByUserId(authentication.getName()).get())
              .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

      if (!refreshToken.getToken().equals(tokenDto.getRefreshToken())) {
        throw new RuntimeException("Refresh Token이 일치하지 않습니다.");
      }

      UserEntity member = authRepository.findByUserId(refreshToken.getUsers().getUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));
      TokenDto tokenDtoResult = tokenProvider.createTokenDto(member.getUserId(), member.getRoles());

      RefreshTokenEntity newRefreshToken = refreshToken.updateValue(tokenDtoResult.getRefreshToken());
      refreshTokenRepository.save(newRefreshToken);

      return tokenDto;
    }
}
