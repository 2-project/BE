package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "product_image_table")
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_cid")
    @Schema(description = "상품 이미지 고유 아이디")
    private Integer productImageCid;

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

    @Column(name = "created_at")
    @Schema(description = "상품 이미지 생성일", example = "1111-11-11 ( 자동생성 )")
    private Date createdAt;

    @Column(name = "updated_at")
    @Schema(description = "상품 이미지 수정일", example = "1111-11-11 ( 자동생성 )")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
