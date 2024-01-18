package com.github.backendpart.repository;

import com.github.backendpart.web.entity.users.RefreshTokenEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {

    Optional<RefreshTokenEntity> findByUsers(UserEntity users);
}
