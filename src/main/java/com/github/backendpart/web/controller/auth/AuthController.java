package com.github.backendpart.web.controller.auth;

import com.github.backendpart.config.security.util.SecurityUtil;

import com.github.backendpart.service.auth.TokenService;
import com.github.backendpart.service.auth.UsersService;
import com.github.backendpart.web.dto.users.*;
import com.github.backendpart.web.entity.users.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "접속관련 api", description = "유저 접속 관련")
public class AuthController {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Operation(summary = "회원가입 요청", description = "회원가입을 한다.")
    @PostMapping("/users/signup")
    public ResponseEntity<String> userSignUp(@RequestBody RequestUserDto requestUserDto){
        log.info("signup controller 진입");
        requestUserDto.setUserPwd(passwordEncoder.encode(requestUserDto.getUserPwd()));
        usersService.signup(requestUserDto);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Operation(summary = "로그인", description = "사용자가 로그인을 한다.")
    @PostMapping("/users/login")
    public ResponseEntity<ResponseTokenDto> userLogin(@RequestBody RequestLoginDto requestLoginDto) {
      log.info("login controller 진입");
      TokenDto tokenDTO = usersService.login(requestLoginDto);
      ResponseCookie responseCookie = ResponseCookie
              .from("refresh_token", tokenDTO.getRefreshToken())
              .httpOnly(true)
              .secure(true)
              .sameSite("None")
              .maxAge(tokenDTO.getDuration())
              .path("/")
              .build();

      ResponseTokenDto tokenResponseDTO = ResponseTokenDto.builder()
              .accessToken(tokenDTO.getAccessToken())
              .refreshToken(tokenDTO.getRefreshToken())
              .build();

      return ResponseEntity.ok().header("Set-Cookie", responseCookie.toString()).body(tokenResponseDTO);
    }

    @Operation(summary = "사용자 정보 확인", description = "사용자가 정보를 확인한다.")
    @GetMapping("/getUserData")
    public ResponseEntity<UserDto> loadMemberData() {
      return ResponseEntity.ok(usersService.getUser(SecurityUtil.getCurrentUsername()));
    }

    @Operation(summary = "토큰 갱신")
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody TokenDto tokenDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
      log.info("principalDetails : {}", customUserDetails.toString());

      TokenDto result = tokenService.refresh(tokenDTO, customUserDetails);
      return ResponseEntity.ok(result);
    }
}
