package com.github.backendpart.repository;

import com.github.backendpart.web.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {
}
