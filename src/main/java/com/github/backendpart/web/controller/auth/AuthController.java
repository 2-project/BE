package com.github.backendpart.web.controller.auth;

import com.github.backendpart.service.auth.AuthService;
import com.github.backendpart.service.auth.UsersService;
import com.github.backendpart.web.dto.users.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "접속관련 api", description = "유저 접속 관련")
public class AuthController {
    private final AuthService authService;
    private final UsersService usersService;

    @Operation(summary = "회원가입 요청", description = "회원가입을 한다.")
//    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody RequestUserDto requestUserDto){
        log.info("[POST] signup controller 진입");

//        authService.signup(requestUserDto, multipartFile);
        authService.signup(requestUserDto);
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }
    @Operation(summary = "이메일 중복 확인")
    @PostMapping("/singup/{userEmail}")
    public ResponseEntity<String> emailCkeck(@PathVariable String userEmail){
        log.info("[POST] email check cotroller 진입");
        String result = authService.userIdCheck(userEmail);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "로그인", description = "사용자가 로그인을 한다.")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> userLogin(@RequestBody RequestLoginDto requestLoginDto, HttpServletResponse httpServletResponse) {
      log.info("login controller 진입");
      TokenDto tokenDTO = authService.login(requestLoginDto);
      ResponseCookie responseCookie = ResponseCookie
              .from("refresh_token", tokenDTO.getRefreshToken())
              .httpOnly(true)
              .secure(true)
              .sameSite("None")
              .maxAge(tokenDTO.getDuration())
              .path("/")
              .build();

//      httpServletResponse.setHeader("Authorization", tokenDTO.getAccessToken());
      httpServletResponse.setHeader("Set-Cookie", responseCookie.toString());

      log.info("access token : {}", tokenDTO.getAccessToken());
      log.info("refresh token: {}", tokenDTO.getRefreshToken());
      return ResponseEntity.ok(tokenDTO);
    }

    @Operation(summary = "토큰 갱신")
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody RequestTokenDto tokenDTO) {
      TokenDto tokenDto = authService.refresh(tokenDTO);
      return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "해당 아이디 유저 로그인 정보")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDto> getUserInfo(@PathVariable String userId) {
      return ResponseEntity.ok(usersService.getLoginUserInfo(userId));
    }

    @Operation(summary = "현재 로그인 정보")
    @GetMapping("/myInfo")
    public ResponseEntity<ResponseUserDto> getUserInfo() {
      return ResponseEntity.ok(usersService.getLoginUserInfo());
    }
}
