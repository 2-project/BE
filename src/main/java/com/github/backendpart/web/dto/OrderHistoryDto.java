package com.github.backendpart.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Setter
public class OrderHistoryDto {
    private List<String> productImages;

    private String productName;

    private String size;

    private Integer quantity;

    private Integer productPrice;

    private LocalDateTime orderAt;

    @Override
    public String toString() {
        return "OrderHistoryDto{" +
                ", productImages='" + productImages + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", orderAt='" + orderAt + '\'' +
                '}';
    }
}
