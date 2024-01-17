package com.github.backendpart.repository;

import com.github.backendpart.web.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
