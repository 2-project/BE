package com.github.backendpart.web.entity;


import com.github.backendpart.web.entity.users.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Builder
@AllArgsConstructor
@Table(name = "user_cart_table")
public class UserCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cart_cid")
    @Schema(description = "장바구니 고유 번호", example = "1")
    private Long userCartCid;

    @OneToOne
    @JoinColumn(name = "user_cid")
    @Schema(description = "유저")
    private UserEntity user;

    @OneToMany(mappedBy = "userCart", fetch = FetchType.EAGER)
    private List<CartEntity> cartList;

}
