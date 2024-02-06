package com.github.backendpart.repository;

import com.github.backendpart.web.dto.product.getProduct.GetProductResponseDto;
import com.github.backendpart.web.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategory_CategoryName(String categoryName);

    Optional<ProductEntity> findByProductCid(Long productId);
}
