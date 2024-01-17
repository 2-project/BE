package com.github.backendpart.web.dto.product;

import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class ProductDto {
    @Schema(description = "상품 고유 아이디", example = "1")
    private Long productCid;

// UserDto가 없어서 일단 주석처리 후에 필요하지 않다면 삭제도 생각중입니다.
//    @Schema(description = "상품등록 유저 정보")
//    private UserDTO user;

    @Schema(description = "상품 이름", example = "상품 A")
    private String productName;

    @Schema(description = "상품 상세 설명", example = "상품 설명이 들어가는 칸 입니다.")
    private String productDescription;

    @Schema(description = "상품 가격", example = "198000")
    private Integer productPrice;

    @Schema(description = "상품 옵션")
    private OptionDto options;

    private List<ProductImageDto> productImages;

    @Schema(description = "상품 카테고리", example = "인기상품")
    private CategoryDto category;

//    @Schema(description = "상품 판매 시작일", example = "1111-11-11")
//    private Date productSaleStart;
//
//    @Schema(description = "상품 판매 종료일", example = "1111-11-11")
//    private Date productSaleEnd;

    public static ProductDto toDto(ProductEntity productEntity){
        //Builder로 구현하고 싶었는데 리스트라..
        ProductDto productDto = new ProductDto();

        productDto.setProductCid(productEntity.getProductCid());
        productDto.setProductDescription(productEntity.getProductDescription());
        productDto.setOptions(OptionDto.toDto(productEntity.getOptions()));

        for(ProductImageEntity productImage : productEntity.getProductImages()){
            productDto.getProductImages().add(ProductImageDto.toDto(productImage));
        }

        productDto.setCategory(CategoryDto.toDto(productEntity.getCategory()));

        return productDto;

//
//        return ProductDto.builder()
//                .productCid(productEntity.getProductCid())
//                .productDescription(productEntity.getProductDescription())
//                .options(OptionDto.toDto(productEntity.getOptions()))
//                //for문으로 돌리자..
//                .productImages(ProductImageDto.toDto(productEntity.getProductImages()))
//                .category(productEntity.getCategory())
//                .build();

    }

}