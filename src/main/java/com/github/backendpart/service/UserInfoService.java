package com.github.backendpart.service;


import com.github.backendpart.repository.UserInfoRepository;
import com.github.backendpart.web.dto.UserInfoDto;
import com.github.backendpart.web.entity.users.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;



@Builder
@AllArgsConstructor
@Service
public class UserInfoService {
    private UserInfoRepository userInfoRepository;

    public UserInfoDto getUserInfoByUserId(Long userCid) {
        UserEntity userEntity = userInfoRepository.findById(userCid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with userCid: " + userCid));

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
