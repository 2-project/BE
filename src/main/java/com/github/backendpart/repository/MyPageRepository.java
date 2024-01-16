package com.github.backendpart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyPageRepository extends JpaRepository<User, Long> {
}
