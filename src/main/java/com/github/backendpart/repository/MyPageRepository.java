package com.github.backendpart.repository;

import com.github.backendpart.web.entity.OrderEntity;
import com.github.backendpart.web.entity.UserCartEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MyPageRepository extends JpaRepository<UserCartEntity, Long> {

    Optional<UserCartEntity> findByUser(UserEntity userEntity);
}
