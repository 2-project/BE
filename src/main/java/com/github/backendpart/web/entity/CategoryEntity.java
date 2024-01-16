package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "category_table")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_cid")
    @Schema(description = "카테고리 고유 아이디")
    private Integer categoryCid;

    @OneToMany(mappedBy = "category_cid")
    private List<ProductEntity> products;

    @Column(name = "category_name")
    @Schema(description = "카테고리 이름",example = "인기상품")
    private String categoryName;

    @Column(name = "created_at")
    @Schema(description = "생성일")
    private Date createdAt;

    @Column(name = "updated_at")
    @Schema(description = "수정일")
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
