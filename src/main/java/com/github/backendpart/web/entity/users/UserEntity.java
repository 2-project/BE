package com.github.backendpart.web.entity.users;

import com.github.backendpart.web.entity.TimeEntity;
import com.github.backendpart.web.entity.enums.Roles;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_table")
public class UserEntity extends TimeEntity {
    @Id
    @Column(name = "user_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "유저 고유 아이디")
    private Long userCid;

    @NotNull
    @Column(name = "user_id", length = 30)
    @Schema(description = "유저 아이디(이메일)", example = "supercoding@admin.com")
    private String userId;

    @NotNull
    @Column(name = "user_pwd")
    @Schema(description = "유저 비밀번호", example = "qwer1234")
    private String userPwd;

    @Column(name = "user_name", length = 30)
    @Schema(description = "유저 이름", example = "판매자")
    private String userName;

    @Column(name = "user_phone", length = 30)
    @Schema(description = "휴대폰번호", example = "010-1111-2222")
    private String userPhone;

    @Column(name = "user_address", length = 100)
    @Schema(description = "주소", example = "어디일까요~")
    private String userAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_cid", referencedColumnName = "profile_image_cid")
    private ProfileImageEntity profileImage;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    @Column(name = "is_deleted", length = 20)
    private String isDeleted;

    @Builder
    public UserEntity(String userId, String userPwd, String userName, String userPhone, String userAddress, String isDeleted){
        this.userId = userId;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.roles = Roles.ROLE_USER;
        this.isDeleted = isDeleted;
    }
}