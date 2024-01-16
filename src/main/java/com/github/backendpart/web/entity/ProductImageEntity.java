package com.github.backendpart.web.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "product_image_table")
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_cid")
    private Integer productImageCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", referencedColumnName = "product_cid")
    private ProductEntity product;

    @Column(name = "product_image_name")
    private String productImageName;

    @Column(name = "product_image_path")
    private String productImagePath;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
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
