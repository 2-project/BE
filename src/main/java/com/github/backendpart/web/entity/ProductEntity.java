package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_table")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_cid")
    @Schema(description = "상품 고유 아이디", example = "1")
    private Integer productCid;

    @ManyToOne
    @JoinColumn(name = "user_cid", referencedColumnName = "user_cid")
    @Schema(description = "상품등록 유저 정보")
    private UserEntity user;

    @Column(name = "product_name")
    @Schema(description = "상품 이름", example = "상품 A")
    private String productName;

    @Column(name = "product_description", columnDefinition = "text")
    @Schema(description = "상품 상세 설명", example = "상품 설명이 들어가는 칸 입니다.")
    private String productDescription;

    @Column(name = "product_price")
    @Schema(description = "상품 가격", example = "198000")
    private Integer productPrice;

    @Column(name = "product_sale_start")
    @Schema(description = "상품 판매 시작일", example = "1111-11-11")
    private Date productSaleStart;

    @Column(name = "product_sale_end")
    @Schema(description = "상품 판매 종료일", example = "1111-11-11")
    private Date productSaleEnd;

    @OneToMany(mappedBy = "product_cid")
    @Schema(description = "상품 옵션")
    private List<OptionEntity> options;

    @OneToMany(mappedBy = "product_cid")
    private List<ProductImageEntity> productImages;

    @ManyToOne
    @JoinColumn(name = "category_cid", referencedColumnName = "category_cid")
    @Schema(description = "상품 카테고리", example = "인기상품")
    private CategoryEntity category;

    @Column(name = "created_at")
    @Schema(description = "상품 생성일", example = "1111-11-11 ( 자동생성 )")
    private Date createdAt;

    @Column(name = "updated_at")
    @Schema(description = "상품 수정일", example = "1111-11-11 ( 자동생성 )")
    private Date updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = new Date();
    }
}
