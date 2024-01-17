package com.github.backendpart.web.entity.users;

import com.github.backendpart.web.entity.TimeEntity;
import com.github.backendpart.web.entity.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "users_table")
public class UserEntity extends TimeEntity {
    @Id
    @Column(name = "user_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCid;

    @NotNull
    @Column(name = "user_id", length = 30)
    private String userId;

    @NotNull
    @Column(name = "user_pwd")
    private String userPwd;

    @Column(name = "user_name", length = 30, columnDefinition = "DEFAULT NULL")
    private String userName;

    @Column(name = "user_phone", length = 30, columnDefinition = "DEFAULT NULL")
    private String userPhone;

    @Column(name = "user_address", length = 100, columnDefinition = "DEFAULT NULL")
    private String userAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_cid", referencedColumnName = "profile_image_cid")
    private ProfileImageEntity profileImage;

    @Enumerated(EnumType.STRING)
    private List<Roles> roles;

    @Column(name = "is_deleted", length = 20)
    private String isDeleted;
}