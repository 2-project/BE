package com.github.backendpart.service;

import com.github.backendpart.repository.ProductRepository;
import com.github.backendpart.web.dto.product.ProductDto;
import com.github.backendpart.web.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductDto findById(long productId){
        ProductEntity targetProduct = productRepository.findById(productId).orElse(null);
        ProductDto targetProductDto = ProductDto.toDto(targetProduct);
        return targetProductDto;
    }

}
