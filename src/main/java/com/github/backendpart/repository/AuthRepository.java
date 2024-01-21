package com.github.backendpart.repository;

import com.github.backendpart.web.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);

    @Query("select m from UserEntity m left join fetch m.profileImage where m.userId = :userId")
    Optional<UserEntity> findByUserIdEagerLoadImage(String userId);
    boolean existsByUserId(String userId);
}
