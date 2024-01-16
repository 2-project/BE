package com.github.backendpart.web.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_table")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_cid")
    private Integer productCid;

    @ManyToOne
    @JoinColumn(name = "user_cid", referencedColumnName = "user_cid")
    private UserEntity user;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description", columnDefinition = "text")
    private String productDescription;

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_sale_start")
    private Date productSaleStart;

    @Column(name = "product_sale_end")
    private Date productSaleEnd;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
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
