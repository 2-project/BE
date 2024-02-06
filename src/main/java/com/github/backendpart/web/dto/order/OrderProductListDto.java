package com.github.backendpart.web.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductListDto {
    @Schema(description = "물품 이름",nullable = false,example = "XX바지")
    private String productname;
    @Schema(description = "옵션",nullable = false,example = "S")
    private String size;
    @Schema(description = "주문 개수",nullable = false,example = "3")
    private int quantity;
    @Schema(description = "물품 가격",nullable = false,example = "15000")
    private int price;
    @Schema(description = "물품 대표사진",nullable = false,example = "주황버섯.png")
    private String imagename;
    @Schema(description = "물품 이미지주소",nullable = false,example = "https://qewr.com")
    private String imageAddress;
    @Schema(description = "물품 상세설명",nullable = false,example = "주황버섯입니다.")
    private String productDescription;
}

