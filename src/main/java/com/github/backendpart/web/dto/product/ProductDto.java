package com.github.backendpart.web.dto.product;

import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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

    public static ProductEntity toEntity(ProductDto productDto){
        List<OptionEntity> optionEntityList = new ArrayList<>();
        if (productDto.getOptions() != null) {
            for (OptionDto option : productDto.getOptions()) {
                OptionEntity optionEntity = OptionDto.toEntity(option);
                optionEntityList.add(optionEntity);
            }
        }

        List<ProductImageEntity> productImageEntityList = new ArrayList<>();
        if (productDto.getProductImages() != null) {
            for (ProductImageDto productImage : productDto.getProductImages()) {
                ProductImageEntity productImageEntity = ProductImageDto.toEntity(productImage);
                productImageEntityList.add(productImageEntity);
            }
        }

        return ProductEntity.builder()
                .productCid(productDto.getProductCid())
                .productName(productDto.getProductName())
                .productDescription(productDto.getProductDescription())
                .productPrice(productDto.getProductPrice())
                .productSaleStart(productDto.getProductSaleStart())
                .productSaleEnd(productDto.getProductSaleEnd())
                .options(productDto.getOptions() == null ? null : optionEntityList)
                .productImages(productDto.getProductImages() ==null ? null :productImageEntityList)
                .category(CategoryDto.toEntity(productDto.getCategory()))
                .build();
    }

    public static ProductDto toDto(ProductEntity productEntity){

        log.info("[toDto] 실행되고 있습니다.");
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


        return ProductDto.builder()
                .productCid(productEntity.getProductCid())
                .productName(productEntity.getProductName())
                .productDescription(productEntity.getProductDescription())
                .productPrice(productEntity.getProductPrice())
                .productSaleStart(productEntity.getProductSaleStart())
                .productSaleEnd(productEntity.getProductSaleEnd())
                .options(productEntity.getOptions() == null ? null : optionDtoList)
                .productImages(productEntity.getProductImages() == null ? null : productImageDtoList)
                .category(CategoryDto.toDto(productEntity.getCategory()))
                .build();
    }
}
