package com.github.backendpart.web.dto.product;

import com.github.backendpart.repository.ProductImageRepository;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class ProductImageDto {

    @Schema(description = "상품 이미지 고유 아이디")
    private Long productImageCid;

    @Schema(description = "상품 Entity")
    private ProductDto product;

    @Schema(description = "상품 이미지 이름", example = "주황버섯.png")
    private String productImageName;

    @Schema(description = "상품 이미지 url", example = "https://qewr.com")
    private String productImagePath;

    public static ProductImageEntity toEntity(ProductImageDto productImageDto){

        return ProductImageEntity.builder()
                .productImageCid(productImageDto.getProductImageCid())
                .product(ProductDto.toEntity(productImageDto.getProduct()))
                .productImageName(productImageDto.getProductImageName())
                .productImagePath(productImageDto.getProductImagePath())
                .build();
    }

    public static ProductImageDto toDto(ProductImageEntity productImageEntity){
        return ProductImageDto.builder()
                .productImageCid(productImageEntity.getProductImageCid())
                .productImagePath(productImageEntity.getProductImagePath())
                .productImageName(productImageEntity.getProductImageName())
                .product(ProductDto.toDto(productImageEntity.getProduct()))
                .build();
    }
}
