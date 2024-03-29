package com.github.backendpart.repository;

import com.github.backendpart.web.entity.CartEntity;
import com.github.backendpart.web.entity.OrderEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {

    boolean existsCartEntityByProductAndUserCartAndCartStatus(ProductEntity product, UserCartEntity usercart,String status);

    CartEntity findByProductAndUserCartAndCartStatus(ProductEntity product, UserCartEntity userCart,String status);

    List<CartEntity> findAllByUserCart(UserCartEntity userCart);

    List<CartEntity> findAllByOrder(OrderEntity order);

    CartEntity findByProductAndCartStatus(ProductEntity product, String status);

    void deleteByProductAndCartStatusAndUserCart(ProductEntity product, String 주문_전, UserCartEntity userCart);
}
