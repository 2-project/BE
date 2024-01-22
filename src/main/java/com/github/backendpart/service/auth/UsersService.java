package com.github.backendpart.service.auth;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backendpart.config.security.util.SecurityUtil;
import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.web.dto.users.ResponseUserDto;
import com.github.backendpart.web.entity.users.CustomUserDetails;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
      UserEntity user = authRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("userId: " + userId + "를 데이터베이스에서 찾을 수 없습니다."));

      // DB 에 user 값이 존재한다면 userDetails 객체로 만들어서 리턴
      GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRoles().toString());

      CustomUserDetails userDetails = CustomUserDetails.builder()
              .userId(user.getUserId())
              .userPwd(user.getUserPwd())
              .userPhone(user.getUserPhone())
              .userAddress(user.getUserAddress())
              .role((Collection<GrantedAuthority>) grantedAuthority)
              .build();

      return userDetails;
    }

    private UserDetails createUserDetails(UserEntity users){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(users.getRoles().getType().toString());

        return new User(
                users.getUserId(),
                users.getUserPwd(),
                Collections.singleton(grantedAuthority)
        );
    }

    @Transactional(readOnly = true)
    public ResponseUserDto getLoginUserInfo(){
      return authRepository.findByUserId(SecurityUtil.getCurrentUserId())
              .map(ResponseUserDto::responseUserDto)
              .orElseThrow(() -> new NotFoundException("로그인 유저 정보가 없습니다."));
    }

    @Transactional(readOnly = true)
    public ResponseUserDto getLoginUserInfo(String userId){
      return authRepository.findByUserId(userId)
              .map(ResponseUserDto::responseUserDto)
              .orElseThrow(() -> new NotFoundException("유저 정보가 없습니다."));
    }
}
