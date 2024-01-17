package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_image_table")
public class ProductImageEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_cid")
    @Schema(description = "상품 이미지 고유 아이디")
    private Long productImageCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", referencedColumnName = "product_cid")
    @Schema(description = "상품 Entity")
    private ProductEntity product;

    @Column(name = "product_image_name")
    @Schema(description = "상품 이미지 이름", example = "주황버섯.png")
    private String productImageName;

    @Column(name = "product_image_path")
    @Schema(description = "상품 이미지 url", example = "https://qewr.com")
    private String productImagePath;

}
