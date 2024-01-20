package com.github.backendpart.web.controller;

import com.github.backendpart.service.OrderService;
import com.github.backendpart.web.dto.order.OrderDto;
import com.github.backendpart.web.dto.order.PayInfoDto;
import com.github.backendpart.web.dto.order.OrderProductListDto;
import com.github.backendpart.web.dto.order.PayOrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name="OrderProduct",description = "Order API Document")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    @Operation(summary = "주문하기", description = "장바구니에 담긴 물품들을 주문한다.")
    @PostMapping("")
    public OrderDto orderCart(){
        return orderService.orderCart();
    }

    @Operation(summary = "결제하기", description = "배송정보를 입력하고 결제완료한다")
    @PostMapping("/{orderId}")
    public PayOrderDto payOrder(@PathVariable Long orderId, PayInfoDto payInfoDto){
        return orderService.payOrder(orderId, payInfoDto);
    }
}
