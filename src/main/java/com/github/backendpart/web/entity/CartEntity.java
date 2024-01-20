package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Table(name = "cart_table")
public class CartEntity extends TimeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "cart_cid")
        @Schema(description = "장바구니 개별 번호", example = "1")
        private Long cartCid;

        @ManyToOne
        @JoinColumn(name = "user_cart_cid")
        @Schema(description = "유저의 장바구니")
        private UserCartEntity userCart;

        @ManyToOne
        @JoinColumn(name = "product_cid", referencedColumnName = "product_cid")
        @Schema(description = "물품")
        private ProductEntity product;

        @ManyToOne
        @JoinColumn(name = "option_cid", referencedColumnName = "option_cid")
        @Schema(description = "물품 옵션")
        private OptionEntity option;

        @Column(name = "cart_status")
        @Schema(description = "장바구니 물품 상태", example = "주문완료")
        private String cartStatus;

        @Column(name = "cart_quantity")
        @Schema(description = "장바구니 물품 수량", example = "5")
        private Integer cartQuantity;

        @ManyToOne
        @JoinColumn(name = "order_cid", referencedColumnName = "order_cid",nullable = true)
        @Schema(description = "주문 번호")
        private OrderEntity order;
    }
