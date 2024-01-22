package com.github.backendpart.service;


import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.repository.CartRepository;
import com.github.backendpart.repository.OrderRepository;
import com.github.backendpart.repository.UserCartRepository;
import com.github.backendpart.web.entity.CartEntity;
import com.github.backendpart.web.entity.UserCartEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UnregisterService {
    private AuthRepository authRepository;
    private UserCartRepository userCartRepository;
    private CartRepository cartRepository;

    @Transactional
    public void deleteUser(Long userId, String password) {
        UserEntity userEntity = authRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));

        if (!userEntity.getUserPwd().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<UserCartEntity> userCarts = userCartRepository.findByUser(userEntity);
        for (UserCartEntity userCart : userCarts) {
            List<CartEntity> cartList = userCart.getCartList();
            for (CartEntity cart : cartList) {
                cartRepository.delete(cart);
            }
            userCartRepository.delete(userCart);
        }

        authRepository.delete(userEntity);
    }
}
