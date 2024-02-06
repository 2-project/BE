package com.github.backendpart.web.controller;


import com.github.backendpart.service.OrderHistoryService;
import com.github.backendpart.web.dto.OrderHistoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "주문 내역 API", description = "주문 내역 조회 API입니다.")
public class OrderHistoryController {
    private OrderHistoryService orderHistoryService;

    @Operation(summary = "주문 내역 조회", description = "유저의 주문 내역을 조회한다.")
    @GetMapping("/order-history/{userCartId}")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(@PathVariable Long userCartId) {
        log.info("GET 주문내역 조회 요청이 들어왔습니다.");
        List<OrderHistoryDto> orderHistory = orderHistoryService.getOrderHistory(userCartId);
        log.info("GET 주문내역 조회 요청 결과: " + orderHistory);
        return ResponseEntity.ok(orderHistory);
    }
}
