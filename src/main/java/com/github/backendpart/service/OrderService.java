package com.github.backendpart.service;

import com.github.backendpart.mapper.OrderMapper;
import com.github.backendpart.repository.*;
import com.github.backendpart.web.dto.order.OrderDto;
import com.github.backendpart.web.dto.order.PayInfoDto;
import com.github.backendpart.web.dto.order.OrderProductListDto;
import com.github.backendpart.web.dto.order.PayOrderDto;
import com.github.backendpart.web.entity.*;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {


    private final UserInfoRepository userInfoRepository;
    private final UserCartRepository userCartRepository;
    private final CartRepository cartRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;


    public OrderDto orderCart() {
            // <장바구니에 담긴 물품 목록을 주문페이지에서도 보여주는 로직>
            // 1. 토큰에서 유저정보 빼오기
//        Long userId = 토큰.getUserId;
            Long userId = 1l;
            UserEntity user = userInfoRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다.: " + userId));

            List<CartEntity> cartList = userCartRepository.findUserCartEntityByUser(user).getCartList();
            // 2. "주문 완료"된 장바구니는 포함시키지 않아야함
            List<CartEntity> NowCartList = cartList.stream().filter(cartEntity -> cartEntity.getCartStatus().equals("주문 전")).toList();
            List<ProductEntity> productList = NowCartList.stream().map(CartEntity::getProduct).toList();
            // 3. 첫번째 이미지 주소와 이름 빼오기(대표사진)
            List<String> imgAddress = productList.stream().map(product -> product.getProductImages().get(0).getProductImagePath()).toList();
            List<String> imgName = productList.stream().map(product -> product.getProductImages().get(0).getProductImageName()).toList();
            log.info("[order] 주문목록의 물품 정보를 가져왔습니다.");
            // 4. 리스트에서 카트 하나씩 빼와서 mapper 및 for문으로 dto로 바꾸기
            List<OrderProductListDto> orderListDTO = NowCartList.stream().map(OrderMapper.INSTANCE::CartEntityToDTO).toList();

            for (int i = 0; i < orderListDTO.size(); i++) {
                orderListDTO.get(i).setImagename(imgAddress.get(i));
                orderListDTO.get(i).setImageAddress(imgName.get(i));
            }

            // <주문 테이블 생성 로직>
            // 1. 새로운 order Entity 생성
            OrderEntity order = new OrderEntity(null, "주문완료");
            OrderEntity newOrder = orderRepository.save(order);
            Long orderId = newOrder.getOrderCid();
            // 2. 장바구니 상태를 "주문완료"상태로 만들기, 장바구니에 주문 엔티티 넣기
            for (CartEntity cart : NowCartList) {
                cart.setCartStatus("주문완료");
                cart.setOrder(order);
                cartRepository.save(cart);
            }
            log.info("[order] 장바구니 물품들이 주문완료되어 주문 번호가 생성되었습니다.");

            // 3. orderDto 반환
            return OrderDto.builder().orderId(orderId).orderProductList(orderListDTO).build();

    }


    public PayOrderDto payOrder(Long orderId, PayInfoDto payInfoDto) {
    try {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException("해당 주문내역을 찾을 수 없습니다.: " + orderId));

        order.setOrderStatus("결제완료");
        order.setRecipient(payInfoDto.getRecipientName());
        order.setShippingAddress(payInfoDto.getAddress());
        order.setPhoneNum(payInfoDto.getPhoneNum());

        orderRepository.save(order);
        log.info("[PayOrder] 결제가 완료되었습니다.");
        // orderdto에서 해당 배송지를 기본배송지로 등록 true했다면, 유저의 배송지 바꾸기
        if (payInfoDto.isDefaultAddress()) {
            Long userId = 1l;
            UserEntity user = userInfoRepository.findById(userId).orElseThrow();
            user.setUserAddress(payInfoDto.getAddress());
            userInfoRepository.save(user);
            log.info("[User] 회원의 기본배송지가 변경되었습니다.");
        }

//      "결제완료"상태 되면 옵션의 재고에서 주문 수량 빼기
        List<CartEntity> cartList = cartRepository.findAllByOrder(order);
        List<OptionEntity> optionList = cartList.stream().map(CartEntity::getOption).toList();

        for (int i = 0; i < optionList.size(); i++) {
            int remainstock = optionList.get(i).getOptionStock() - cartList.get(i).getCartQuantity();
            optionList.get(i).setOptionStock(remainstock);
            optionRepository.save(optionList.get(i));
        }
        log.info("[Option] 제품이 판매되어 해당 옵션의 재고가 수정되었습니다.");

        return PayOrderDto.builder().code(200).success(true).message("결제 완료되었습니다!").build();
    }
    catch (Exception e){
        log.error("[ERROR] 에러가 발생했습니다. :" +e);
        return PayOrderDto.builder().code(400).success(false).message("결제 실패하였습니다!").build();
    }
    }
}

