package com.github.backendpart.web.dto.product.addProduct;

import com.github.backendpart.web.dto.product.CategoryDto;
import com.github.backendpart.web.dto.product.OptionDto;
import com.github.backendpart.web.dto.product.ProductDto;
import com.github.backendpart.web.dto.product.ProductImageDto;
import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class AddProductRequestDto {
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
    private List<OptionRequestDto> options;

    @Schema(description = "상품 카테고리", example = "인기상품")
    private String category;

    public static AddProductRequestDto toDto(ProductEntity productEntity){
        log.info("[toDto] 실행되고 있습니다.");
        List<OptionRequestDto> optionDtoList = new ArrayList<>();
        if (productEntity.getOptions() != null) {
            for (OptionEntity option : productEntity.getOptions()) {
                OptionRequestDto optionDto = OptionRequestDto.toDto(option);
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


        return AddProductRequestDto.builder()
                .productName(productEntity.getProductName())
                .productDescription(productEntity.getProductDescription())
                .productPrice(productEntity.getProductPrice())
                .productSaleStart(productEntity.getProductSaleStart())
                .productSaleEnd(productEntity.getProductSaleEnd())
                .options(productEntity.getOptions() == null ? null : optionDtoList)
                //.productImages(productEntity.getProductImages() == null ? null : productImageDtoList)
                .category(productEntity.getCategory().getCategoryName())
                .build();
    }


}
