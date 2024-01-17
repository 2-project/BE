package com.github.backendpart.web.dto.product;

import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class ProductImageDto {
    @Schema(description = "상품 이미지 고유 아이디")
    private Long productImageCid;

    @Schema(description = "상품 Entity")
    private long productCid;

    @Schema(description = "상품 이미지 이름", example = "주황버섯.png")
    private String productImageName;

    @Schema(description = "상품 이미지 url", example = "https://qewr.com")
    private String productImagePath;

    public static ProductImageDto toDto(ProductImageEntity productImageEntity) {
        return ProductImageDto.builder()
                .productImageCid(productImageEntity.getProductImageCid())
                .productCid(productImageEntity.getProduct().getProductCid())
                .productImageName(productImageEntity.getProductImageName())
                .productImagePath(productImageEntity.getProductImagePath())
                .build();
    }
}