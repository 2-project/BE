package com.github.backendpart.service.auth;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backendpart.config.security.JwtTokenProvider;
import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.repository.RefreshTokenRepository;
import com.github.backendpart.service.ImageUploadService;
import com.github.backendpart.web.dto.users.*;
import com.github.backendpart.web.entity.ProductImageEntity;
import com.github.backendpart.web.entity.enums.Roles;
import com.github.backendpart.web.entity.users.ProfileImageEntity;
import com.github.backendpart.web.entity.users.RefreshToken;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final AuthRepository authRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final ImageUploadService imageUploadService;
  /**
   * UsernamePasswordAuthenticationToken을 통한 Spring Security인증 진행
   * 이후 tokenService에 userId값을 전달하여 토큰 생성
   * @param requestDTO
   * @return TokenDTO
   */

  @Transactional
  public TokenDto login(RequestLoginDto requestDTO) {
      UserEntity users = authRepository.findByUserId(requestDTO.getUserId()).orElseThrow(() -> new NotFoundException("아이디 혹은 비밀번호를 틀리셨습니다."));
      String isDeleted = users.getIsDeleted();
      if(isDeleted != null && isDeleted.equals("deleted"))
        throw new NotFoundException("이미 탈퇴한 계정입니다.");

      if(!passwordEncoder.matches(requestDTO.getUserPwd(), users.getUserPwd()))
        throw new NotFoundException("아이디 혹은 비밀번호가 틀렸습니다.");

      // 1. ID(email)/PW 기반으로 AuthenticationToken 생성
      UsernamePasswordAuthenticationToken authenticationToken = requestDTO.toAuthentication();
      // 2. 실제 검증 로직(사용자 비밀번호 체크)
      // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행됨
      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

      // 3. 인증 정보를 기반으로 JWT토큰 생성
      TokenDto tokenDto = jwtTokenProvider.createTokenDto(authentication);

      // 4. RefreshToken 저장
      RefreshToken refreshToken = RefreshToken.builder()
              .key(authentication.getName())
              .value(tokenDto.getRefreshToken())
              .build();

      refreshTokenRepository.save(refreshToken);

      // 5. 토큰 발급
      return tokenDto;
  }

  @Transactional
  public void signup(RequestUserDto requestDto, MultipartFile multipartFile) {
      if(authRepository.existsByUserId(requestDto.getUserId())) {
          throw new RuntimeException("이미 존재하는 아이디입니다.");
      }
      requestDto.setUserPwd(passwordEncoder.encode(requestDto.getUserPwd()));

      UserEntity user = UserEntity.builder()
              .userId(requestDto.getUserId())
              .userPwd(requestDto.getUserPwd())
              .userName(requestDto.getUserName())
              .userPhone(requestDto.getUserPhone())
              .userAddress(requestDto.getUserAddress())
              .isDeleted(null)
              .build();

      log.info("[build] user = " + user);
      authRepository.save(user);
      // 프로필 이미지가 있다면 추가
      if(multipartFile != null) {
          ProfileImageEntity uploadImages = imageUploadService.profileUploadImage(multipartFile);
          user.setProfileImage(uploadImages);
          authRepository.save(user);
          log.info("[profileImage] 유저프로필 이미지가 추가되었습니다. uploadedImages = " + uploadImages);
      }
  }

  /**
   * 토큰 재발급
   */
  @Transactional
  public TokenDto refresh(RequestTokenDto requestTokenDto) {
      // 1. Refresh Token 검증 (validateToken() : 토큰 검증)
      if(!jwtTokenProvider.validationToken(requestTokenDto.getRefreshToken())) {
        throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
      }

      // 2. Access Token에서 Studio ID 가져오기
      Authentication authentication = jwtTokenProvider.getAuthentication(requestTokenDto.getAccessToken());

      // 3. 저장소에서 Studio ID를 기반으로 Refresh Token값 가져오기
      RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
              .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

      // 4. Refresh Token 일치 여부
      if (!refreshToken.getValue().equals(requestTokenDto.getRefreshToken())) {
        throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
      }

      // 5. 새로운 토큰 생성
      TokenDto tokenDto = jwtTokenProvider.createTokenDto(authentication);

      // 6. 저장소 정보 업데이트
      RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
      refreshTokenRepository.save(newRefreshToken);

      // 토큰 발급
      return tokenDto;
  }
}
