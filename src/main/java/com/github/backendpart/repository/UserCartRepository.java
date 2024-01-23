package com.github.backendpart.repository;

import com.github.backendpart.web.entity.UserCartEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserCartRepository extends JpaRepository<UserCartEntity,Long> {
    UserCartEntity findUserCartEntityByUser(UserEntity user);

    boolean existsUserCartEntityByUser(UserEntity user);

    List<UserCartEntity> findByUser(UserEntity userEntity);
}
