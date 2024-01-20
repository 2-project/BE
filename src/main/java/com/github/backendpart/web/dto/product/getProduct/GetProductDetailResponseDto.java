package com.github.backendpart.web.dto.product.getProduct;

import com.github.backendpart.web.dto.product.CategoryDto;
import com.github.backendpart.web.dto.product.OptionDto;
import com.github.backendpart.web.dto.product.ProductImageDto;
import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Slf4j
@ToString
public class GetProductDetailResponseDto {
    @Schema(description = "상품 고유 아이디", example = "1")
    private Long productCid;

    @Schema(description = "상품 이름", example = "상품 A")
    private String productName;

    @Schema(description = "상품 상세 설명", example = "상품 설명이 들어가는 칸 입니다.")
    private String productDescription;

    @Schema(description = "상품 가격", example = "198000")
    private Integer productPrice;

    @Schema(description = "상품 옵션")
    private List<OptionDto> options;

    private List<ProductImageDto> productImages;

    @Schema(description = "상품 카테고리", example = "인기상품")
    private CategoryDto category;

    public static GetProductDetailResponseDto toDto(ProductEntity productEntity) {
        log.info("[GetProductDetailResponseDto/toDto] 실행되고 있습니다.");
        List<OptionDto> optionDtoList = new ArrayList<>();
        if (productEntity.getOptions() != null) {
            for (OptionEntity option : productEntity.getOptions()) {
                OptionDto optionDto = OptionDto.toDto(option);
                optionDtoList.add(optionDto);
            }
        }

        List<ProductImageDto> productImageDtoList = new ArrayList<>();
        if (productEntity.getProductImages() != null) {
            for (ProductImageEntity productImage : productEntity.getProductImages()) {
                ProductImageDto productImageDto = ProductImageDto.toDto(productImage);
                productImageDtoList.add(productImageDto);
            }
        }


        return GetProductDetailResponseDto.builder()
                .productCid(productEntity.getProductCid())
                .productName(productEntity.getProductName())
                .productDescription(productEntity.getProductDescription())
                .productPrice(productEntity.getProductPrice())
                .options(productEntity.getOptions() == null ? null : optionDtoList)
                .productImages(productEntity.getProductImages() == null ? null : productImageDtoList)
                .category(CategoryDto.toDto(productEntity.getCategory()))
                .build();
    }

}

