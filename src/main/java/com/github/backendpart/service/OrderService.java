package com.github.backendpart.service;

import com.github.backendpart.repository.*;
import com.github.backendpart.web.dto.cart.CartDto;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.order.OrderDto;
import com.github.backendpart.web.dto.order.OrderProductListDto;
import com.github.backendpart.web.dto.order.PayOrderDto;
import com.github.backendpart.web.entity.*;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {


    private final UserInfoRepository userInfoRepository;
    private final UserCartRepository userCartRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;


    public List<OrderProductListDto> getOrderlist() {
        //1. 토큰에서 유저정보 빼오기
//        Long userId = 토큰.getUserId;
        Long userId = 1l;
        UserEntity user = userInfoRepository.findById(userId).orElseThrow();
        List<CartEntity> userCartList = userCartRepository.findUserCartEntityByUser(user).getCartList();


        // 리스트에서 카트 하나씩 빼와서 mapper로 dto로 바꾸기
        List<OrderProductListDto> cartListdto = userCartList.stream().map(OrderMapper.INSTANCE::CartEntityToDTO).collect(Collectors.toList());


        //주문 페이지로 접속한 순간 장바구니 상태를 "주문완료"상태로 만들기
        for(CartEntity cart:userCartList){
            cart.setCartStatus("주문완료");
            cartRepository.save(cart);
        }

        return cartListdto;
    }


    public PayOrderDto orderCart(OrderDto orderDto) {
        // order 테이블 생성
        UserEntity user = userInfoRepository.findById(1L).orElseThrow(); //토큰에서 빼오기
        UserCartEntity userCart = userCartRepository.findUserCartEntityByUser(user);
        OrderEntity order = OrderEntity.builder().orderStatus("결제완료").userCart(userCart)
                .recipient(orderDto.getRecipientName()).phoneNum(orderDto.getPhoneNum())
                .shippingAddress(orderDto.getAddress()).build();

        orderRepository.save(order);
        Long orderId = order.getOrderCid();

        // orderdto에서 해당 배송지를 기본배송지로 등록 true했다면, 유저의 배송지 바꾸기
        if(orderDto.isDefaultAddress()){
            user.setUserAddress(orderDto.getAddress());
        }

        // "결제완료"상태 되면 옵션의 재고에서 주문 수량 빼기
        List<CartEntity> cartList = cartRepository.findAllByUserCart(userCart);  // cartService의 findAllCart 로직하고 비교해보기 -- 하나로 뭉치기
        List<OptionEntity> optionList = cartList.stream().map(CartEntity::getOption).toList();

        for(int i=0;i<optionList.size();i++){
        int remainstock = optionList.get(i).getOptionStock() - cartList.get(i).getCartQuantity();
        optionList.get(i).setOptionStock(remainstock);
        optionRepository.save(optionList.get(i));
         }

        return PayOrderDto.builder().code(200).success(true).message("(주문id: "+orderId+") 결제 완료되었습니다!").orderId(orderId).build();
    }






}

