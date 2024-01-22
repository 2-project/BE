package com.github.backendpart.service.product;

import com.github.backendpart.repository.ProductRepository;
import com.github.backendpart.web.dto.product.getProduct.GetProductDetailResponseDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductResponseDto;
import com.github.backendpart.web.entity.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public GetProductDetailResponseDto findById(Long productId){
        ProductEntity targetProduct = productRepository.findByProductCid(productId).orElseThrow(() -> new IllegalArgumentException("해당 Product가 없습니다"));

        //판매기간만 조회 가능하도록
        if(targetProduct.isSellable()) {
            return GetProductDetailResponseDto.toDto(targetProduct);
        }

        return null;
    }

    @Transactional
    public List<GetProductResponseDto> findByCategory(String categoryName){
        log.info("[GetProduct] 요청이 들어왔습니다");
        List<ProductEntity> targetProductsEntity = productRepository.findByCategory_CategoryName(categoryName);
        List<GetProductResponseDto> targetProductsDto = new ArrayList<>();

        if(targetProductsEntity == null){
            log.info("[GetProduct] 해당 카테고리에 상품이 없습니다");
        }
        else {
            for (ProductEntity productEntity : targetProductsEntity) {
                if(productEntity.isSellable()){
                    targetProductsDto.add(GetProductResponseDto.toDto(productEntity));
                    log.info("[Product] 카테고리 조회 리스트에 추가된 상품 : " + productEntity.getProductName());
                }
            }
        }

        return targetProductsDto;
    }
}
