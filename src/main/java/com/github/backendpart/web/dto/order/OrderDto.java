package com.github.backendpart.web.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    @Schema(description = "수령인",nullable = false,example = "희주")
    private String recipientName;
    @Schema(description = "연락처",nullable = false,example = "010-1234-5678")
    private String phoneNum;
    @Schema(description = "배송지",nullable = false,example = "서울시 동교동")
    private String address;
    @Schema(description = "기본 배송지로 등록하기",nullable = false,example = "true")
    private boolean isDefaultAddress;
    @Schema(description = "배송메시지",nullable = false,example = "잘 배송해주세요~")
    private String message;
}
