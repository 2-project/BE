package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_table")
public class OrderEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_cid")
        @Schema(description = "주문 번호", example = "1")
        private Long orderCid;

        @ManyToOne
        @JoinColumn(name = "user_cart_cid")
        @Schema(description = "유저의 장바구니")
        private UserCartEntity userCart;

        @Column(name = "order_status")
        @Schema(description = "주문 상태", example = "주문 완료")
        private String orderStatus;

        @Column(name = "order_quantity")
        @Schema(description = "주문 수량", example = "주문한 물품 총 수량이 들어가는 곳입니다.")
        private String orderQuantity;

        @Column(name = "order_total_price")
        @Schema(description = "주문 총 가격", example = "198000")
        private Integer orderTotalPrice;

        @Column(name = "created_at")
        @Schema(description = "주문 날짜", example = "1111-11-11")
        private LocalDateTime orderAt;

        @Column(name = "updated_at")
        @Schema(description = "주문 상태 변경 날짜", example = "1111-11-11")
        private LocalDateTime updatedAt;


    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
