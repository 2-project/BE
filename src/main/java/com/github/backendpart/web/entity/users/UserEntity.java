package com.github.backendpart.web.entity.users;

import com.github.backendpart.web.entity.TimeEntity;
import com.github.backendpart.web.entity.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "user_name", length = 30)
    private String userName;

    @Column(name = "user_phone", length = 30)
    private String userPhone;

    @Column(name = "user_address", length = 100)
    private String userAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_cid", referencedColumnName = "profile_image_cid")
    private ProfileImageEntity profileImage;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    @Column(name = "is_deleted", length = 20)
    private String isDeleted;

    @Builder
    public UserEntity(String userId, String userPwd, String userName, String userPhone, String userAddress, Roles roles, String isDeleted){
        this.userId = userId;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.roles = roles;
        this.isDeleted = isDeleted;
    }

    public void updateProfileImage(ProfileImageEntity profileImage) {
        this.profileImage = profileImage;
    }

    public void updateRole(Roles role) {
        if(this.roles == null) {
          this.roles = null;
        }

        else if(this.roles.getType() != null) {
          this.roles = role;
        }
    }
}