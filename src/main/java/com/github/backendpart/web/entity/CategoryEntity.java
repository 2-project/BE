package com.github.backendpart.web.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "category_table")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_cid")
    private Integer categoryCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", referencedColumnName = "product_cid")
    private ProductEntity product;

    @Column(name = "category_name")
    private String categoryName;

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
