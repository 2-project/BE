package com.github.backendpart.service.auth;

import com.github.backendpart.config.security.JwtTokenProvider;
import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.web.dto.users.TokenDto;

import com.github.backendpart.web.entity.users.CustomUserDetails;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final JwtTokenProvider tokenProvider;
    private final AuthRepository authRepository;


    //createToken(users): 일반 로그인 시 Member를 받아와서 토큰을 생성하는 기능
    public TokenDto createToken(UserEntity users) {
      TokenDto tokenDto = tokenProvider.createTokenDto(users.getUserId(), users.getRoles());

      return tokenDto;
    }

    // 클라이언트가 보낸 accessToken이 만료되었을 때 refreshToken을 이용하여 갱신할 수 있도록 하는 기능
    public TokenDto refresh(TokenDto tokenDto, CustomUserDetails customUserDetails) {
      if(!tokenProvider.validationToken(tokenDto.getRefreshToken())) {
        throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
      }

      Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
      log.info("authentication : {}", authentication);

      if (!authentication.equals(tokenDto.getRefreshToken())) {
        throw new RuntimeException("Refresh Token이 일치하지 않습니다.");
      }

      UserEntity users = authRepository.findByUserId(customUserDetails.getUser().getUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));
      TokenDto tokenDtoResult = tokenProvider.createTokenDto(users.getUserId(), users.getRoles());

      return tokenDtoResult;
    }
}
