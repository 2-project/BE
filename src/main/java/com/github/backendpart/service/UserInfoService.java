package com.github.backendpart.service;


import com.github.backendpart.repository.UserInfoRepository;
import com.github.backendpart.web.dto.UserInfoDto;
import com.github.backendpart.web.entity.users.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Builder
@AllArgsConstructor
@Slf4j
@Service
public class UserInfoService {
    private UserInfoRepository userInfoRepository;

    public UserInfoDto getUserInfoByUserId(Long userCid) {
        log.info("GET 회원 고유 아이디 조회");
        UserEntity userEntity = userInfoRepository.findById(userCid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with userCid: " + userCid));
        log.info("GET 회원 고유 아이디: " + userEntity.getUserCid());
        return UserInfoDto.builder()
                .userCid(userEntity.getUserCid())
                .userId(userEntity.getUserId())
                .userPwd(userEntity.getUserPwd())
                .userName(userEntity.getUserName())
                .userPhone(userEntity.getUserPhone())
                .userAddress(userEntity.getUserAddress())
                .build();
    }
}
