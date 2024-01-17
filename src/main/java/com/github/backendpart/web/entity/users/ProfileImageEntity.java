package com.github.backendpart.web.entity.users;

import com.github.backendpart.web.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "profile_image_table")
public class ProfileImageEntity extends TimeEntity {
    @Id
    @Column(name = "profile_image_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileImageCid;

    @Column(name = "profile_image_name")
    private String profileImageName;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "profile_image_type")
    private String profileImageType;
}
