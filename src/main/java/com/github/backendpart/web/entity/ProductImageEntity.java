package com.github.backendpart.web.entity;

import com.github.backendpart.web.dto.product.ProductImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "product_image_table")
public class ProductImageEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_cid")
    @Schema(description = "상품 이미지 고유 아이디")
    private Long productImageCid;

    @Column(name = "product_image_name")
    @Schema(description = "상품 이미지 이름", example = "주황버섯.png")
    private String productImageName;

    @Column(name = "product_image_path")
    @Schema(description = "상품 이미지 url", example = "https://qewr.com")
    private String productImagePath;

    public static ProductImageEntity toEntity(ProductImageDto productImageDto){

        return ProductImageEntity.builder()
                .productImageCid(productImageDto.getProductImageCid())
                .productImageName(productImageDto.getProductImageName())
                .productImagePath(productImageDto.getProductImagePath())
                .build();
    }

}
