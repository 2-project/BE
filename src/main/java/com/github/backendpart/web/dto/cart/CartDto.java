package com.github.backendpart.web.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    @Schema(description = "담겨있는 장바구니 번호",nullable = false,example = "2")
    private long cartId;
    @Schema(description = "물품 이름",nullable = false,example = "XX바지")
    private String productname;
    @Schema(description = "옵션",nullable = false,example = "S")
    private String size;
    @Schema(description = "담은 개수",nullable = false,example = "3")
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
