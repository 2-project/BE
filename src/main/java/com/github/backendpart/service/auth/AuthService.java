package com.github.backendpart.service.auth;

import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.web.dto.users.RequestLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 로그인 시 인증을 도와주는 기능
    public void authenticateLogin(RequestLoginDto requestDTO) {
      UsernamePasswordAuthenticationToken authenticationToken = requestDTO.toAuthentication();
      authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }
}
