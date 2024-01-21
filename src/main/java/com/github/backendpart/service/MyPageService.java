//package com.github.backendpart.service;
//
//
//import com.github.backendpart.repository.AuthRepository;
//import com.github.backendpart.repository.MyPageRepository;
//import com.github.backendpart.repository.OrderRepository;
//import com.github.backendpart.repository.UserCartRepository;
//import com.github.backendpart.web.dto.MyPageDto;
//import com.github.backendpart.web.entity.OrderEntity;
//import com.github.backendpart.web.entity.UserCartEntity;
//import com.github.backendpart.web.entity.users.UserEntity;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import org.springframework.stereotype.Repository;
//import org.springframework.stereotype.Service;
//
//@Service
//@Builder
//@AllArgsConstructor
//public class MyPageService {
//    private MyPageRepository myPageRepository;
//    private AuthRepository authRepository;
//    public MyPageDto getMyPageInfo(Long userCid) {
//        UserEntity userEntity = authRepository.findById(userCid)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with userCid: " + userCid));
//
//        return MyPageDto.builder()
//                .userId(userEntity.getUserId())
//                .userName(userEntity.getUserName())
//                .orderCount(myPageRepository.countByUserCartUserUserCid(userCid))
//                .build();
//    }
//}
