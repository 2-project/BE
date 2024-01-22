package com.github.backendpart.web.controller;


import com.github.backendpart.service.OrderHistoryService;
import com.github.backendpart.web.dto.OrderHistoryDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
public class OrderHistoryController {
    private OrderHistoryService orderHistoryService;

    @GetMapping("/order-history/{userCartId}")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(@PathVariable Long userCartId) {
        log.info("GET 주문내역 조회 요청이 들어왔습니다.");
        List<OrderHistoryDto> orderHistory = orderHistoryService.getOrderHistory(userCartId);
        log.info("GET 주문내역 조회 요청 결과: " + orderHistory);
        return ResponseEntity.ok(orderHistory);
    }
}
