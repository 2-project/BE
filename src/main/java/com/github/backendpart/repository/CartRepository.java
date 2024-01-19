package com.github.backendpart.repository;

import com.github.backendpart.web.entity.CartEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {


    void deleteAllByProduct(ProductEntity product);

    boolean existsCartEntityByProductAndUserCart(ProductEntity product, UserCartEntity usercart);

    CartEntity findByProductAndUserCart(ProductEntity product, UserCartEntity userCart);

    List<CartEntity> findAllByUserCart(UserCartEntity userCart);
}
