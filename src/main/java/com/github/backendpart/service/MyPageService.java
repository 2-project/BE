package com.github.backendpart.service;


import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.repository.MyPageRepository;
import com.github.backendpart.repository.OrderRepository;
import com.github.backendpart.repository.UserCartRepository;
import com.github.backendpart.web.dto.MyPageDto;
import com.github.backendpart.web.entity.CartEntity;
import com.github.backendpart.web.entity.OrderEntity;
import com.github.backendpart.web.entity.UserCartEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final AuthRepository authRepository;

    public MyPageDto getMyPageInfo(Long userId) {
        try {

            UserCartEntity userCart = getUserCartByUserId(userId);

            List<CartEntity> cartList = userCart.getCartList();

            Map<Long, List<CartEntity>> groupedCarts = cartList.stream()
                    .collect(Collectors.groupingBy(cart -> cart.getOrder().getOrderCid()));

            long orderCount = groupedCarts.size();

            return MyPageDto.builder()
                    .userName(userCart.getUser().getUserName())
                    .userId(userCart.getUser().getUserId())
                    .orderCount(orderCount)
                    .build();

        } catch (EntityNotFoundException ex) {
            throw new NotFoundException("사용자 정보를 찾을 수 없습니다.");
        } catch (NullPointerException ex) {
            throw new NotFoundException("주문 정보를 찾을 수 없습니다.");
        }
    }

    public UserCartEntity getUserCartByUserId(Long userId) {
        UserEntity userEntity = authRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));

        return myPageRepository.findByUser(userEntity)
                .orElseThrow(() -> new NotFoundException("해당 사용자의 장바구니 정보를 찾을 수 없습니다."));
    }
}



