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
@ToString
@Slf4j
public class GetProductResponseDto {
    @Schema(description = "상품 고유 아이디", example = "1")
    private Long productCid;

    @Schema(description = "상품 이름", example = "상품 A")
    private String productName;

    @Schema(description = "상품 가격", example = "198000")
    private Integer productPrice;

    private List<ProductImageDto> productImages;

    //필요 없을거같은데 로그용으로
    @Schema(description = "상품 카테고리", example = "인기상품")
    private CategoryDto category;

    public static GetProductResponseDto toDto(ProductEntity productEntity) {
        log.info("[GetProductResponseDto/toDto] 실행되고 있습니다.");
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


        return GetProductResponseDto.builder()
                .productCid(productEntity.getProductCid())
                .productName(productEntity.getProductName())
                .productPrice(productEntity.getProductPrice())
                .productImages(productEntity.getProductImages() == null ? null : productImageDtoList)
                .category(CategoryDto.toDto(productEntity.getCategory()))
                .build();
    }

}
