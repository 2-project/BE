package com.github.backendpart.service;

import com.github.backendpart.repository.CartRepository;
import com.github.backendpart.repository.ProductImageRepository;
import com.github.backendpart.repository.ProductRepository;
import com.github.backendpart.repository.UserCartRepository;
import com.github.backendpart.web.dto.OrderHistoryDto;
import com.github.backendpart.web.entity.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {
    private final UserCartRepository userCartRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public List<OrderHistoryDto> getOrderHistory(Long userCartId) {
        UserCartEntity userCartEntity = userCartRepository.findById(userCartId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저의 장바구니를 찾을 수 없습니다.: " + userCartId));

        List<CartEntity> cartList = userCartEntity.getCartList();
        List<OrderHistoryDto> orderHistoryList = new ArrayList<>();

        for (CartEntity cartEntity : cartList) {
            if ("주문완료".equals(cartEntity.getCartStatus())) {
                ProductEntity productEntity = productRepository.findById(cartEntity.getProduct().getProductCid())
                        .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다.: " + cartEntity.getProduct().getProductCid()));

                List<String> imgAddress = new ArrayList<>();
                if (productEntity.getProductImages() != null && !productEntity.getProductImages().isEmpty()) {
                    imgAddress = productEntity.getProductImages().stream()
                            .map(ProductImageEntity::getProductImagePath)
                            .toList();
                }

                OrderHistoryDto orderHistoryDto = OrderHistoryDto.builder()
                        .productImages(imgAddress)
                        .productName(productEntity.getProductName())
                        .size(cartEntity.getOption().getOptionName())
                        .quantity(cartEntity.getCartQuantity())
                        .productPrice(productEntity.getProductPrice())
                        .orderAt(cartEntity.getOrder().getCreatedAt())
                        .build();

                orderHistoryList.add(orderHistoryDto);
            }
        }

        return orderHistoryList;
    }
}



