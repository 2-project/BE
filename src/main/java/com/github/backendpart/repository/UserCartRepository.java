package com.github.backendpart.repository;

import com.github.backendpart.web.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCartRepository extends JpaRepository<UserCartEntity,Integer> {

}
