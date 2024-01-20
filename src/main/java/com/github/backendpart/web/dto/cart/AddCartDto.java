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
public class AddCartDto {
    @Schema(description = "옵션 고유 번호",nullable = false,example = "1")
    private Long optionid;
    @Schema(description = "담을 수량",nullable = false,example = "3")
    private int quantity;
}
