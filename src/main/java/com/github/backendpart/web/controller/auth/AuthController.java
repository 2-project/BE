package com.github.backendpart.web.controller.auth;

import com.github.backendpart.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "로그인 api", description = "유저 접속 관련")
public class AuthController {
    private final AuthService authService;
}
