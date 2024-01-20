package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
@Table(name = "order_table")
public class OrderEntity extends TimeEntity{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_cid")
        @Schema(description = "주문 번호", example = "1")
        private Long orderCid;

        @Column(name = "order_status")
        @Schema(description = "주문 상태", example = "주문 완료")
        private String orderStatus;

        @Column(name = "recipient")
        @Schema(description = "수령인", example = "홍길동")
        private String recipient;

        @Column(name = "shipping_address")
        @Schema(description = "배송지", example = "경기도 성남시")
        private String shippingAddress;

        @Column(name = "phone_number")
        @Schema(description = "수령인 전화번호", example = "010-1234-5678")
        private String phoneNum;

        public OrderEntity(Object orderCid, String orderStatus) {
                this.orderStatus = orderStatus;
        }
}
