package com.github.backendpart.web.dto.product;

import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class ProductDto {
    @Schema(description = "상품 고유 아이디", example = "1")
    private Long productCid;

    @Schema(description = "상품 이름", example = "상품 A")
    private String productName;

    @Schema(description = "상품 상세 설명", example = "상품 설명이 들어가는 칸 입니다.")
    private String productDescription;

    @Schema(description = "상품 가격", example = "198000")
    private Integer productPrice;

    @Schema(description = "상품 판매 시작일", example = "1111-11-11")
    private Date productSaleStart;

    @Schema(description = "상품 판매 종료일", example = "1111-11-11")
    private Date productSaleEnd;

    @Schema(description = "상품 옵션")
    private List<OptionDto> options;

    private List<ProductImageDto> productImages;

    @Schema(description = "상품 카테고리", example = "인기상품")
    private CategoryDto category;

}
