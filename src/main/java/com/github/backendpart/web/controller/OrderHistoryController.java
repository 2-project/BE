package com.github.backendpart.web.controller;


import com.github.backendpart.service.OrderHistoryService;
import com.github.backendpart.web.dto.OrderHistoryDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class OrderHistoryController {
    private OrderHistoryService orderHistoryService;

    @GetMapping("/order-history/{userCartId}")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(@PathVariable Long userCartId) {
        List<OrderHistoryDto> orderHistory = orderHistoryService.getOrderHistory(userCartId);
        return ResponseEntity.ok(orderHistory);
    }
}
