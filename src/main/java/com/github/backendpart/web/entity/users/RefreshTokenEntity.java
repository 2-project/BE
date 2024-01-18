package com.github.backendpart.web.entity.users;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapping;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "token_table")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_cid")
    private Long tokenCid;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity users;

    private String token;

    @Builder
    public RefreshTokenEntity(UserEntity users, String token) {
      this.users = users;
      this.token = token;
    }

    public RefreshTokenEntity updateValue(String token) {
      this.token = token;
      return this;
    }
}
