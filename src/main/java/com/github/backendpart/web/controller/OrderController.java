package com.github.backendpart.web.controller;

import com.github.backendpart.service.OrderService;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.order.OrderDto;
import com.github.backendpart.web.dto.order.PayInfoDto;
import com.github.backendpart.web.entity.users.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name="주문/결제 API",description = "상품 주문 및 결제를 담당하는 API입니다.")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    @Operation(summary = "주문하기", description = "장바구니에 담긴 물품들을 주문한다.")
    @PostMapping("")
    public ResponseEntity<OrderDto> orderCart(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("[POST] 장바구니 상품 목록 주문 요청 들어왔습니다.");
        OrderDto orderDto = orderService.orderCart(customUserDetails);
        return ResponseEntity.ok().body(orderDto);
    }

    @Operation(summary = "결제하기", description = "배송정보를 입력하고 결제완료한다")
    @PostMapping("/{orderId}")
    public CommonResponseDto payOrder(@PathVariable(name="orderId") Long orderId,
                                      PayInfoDto payInfoDto){
        log.info("[POST] 주문 결제 요청 들어왔습니다.");
        return orderService.payOrder(orderId, payInfoDto);
    }
}
