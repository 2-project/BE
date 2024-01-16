package com.github.backendpart.web.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "option_table")
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_cid")
    private Integer optionCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", referencedColumnName = "product_cid")
    private ProductEntity product;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "option_stock")
    private Integer optionStock;

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
