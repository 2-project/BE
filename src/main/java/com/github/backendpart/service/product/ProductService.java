package com.github.backendpart.service.product;

import com.github.backendpart.repository.CategoryRepository;
import com.github.backendpart.repository.ProductRepository;
import com.github.backendpart.web.dto.product.getProduct.GetProductByCategoryReponseDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductDetailResponseDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductResponseDto;
import com.github.backendpart.web.entity.CartEntity;
import com.github.backendpart.web.entity.CategoryEntity;
import com.github.backendpart.web.entity.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public GetProductDetailResponseDto findById(Long productId){
        ProductEntity targetProduct = productRepository.findByProductCid(productId)
                .orElseThrow(() -> new NoSuchElementException("해당 Product가 없습니다"));

        //판매기간만 조회 가능하도록
        if(targetProduct.isSellable()) {
            return GetProductDetailResponseDto.toDto(targetProduct);
        }

        return null;
    }

    public GetProductByCategoryReponseDto findByCategory(String categoryName){
        log.info("[GetProduct] findByCategory 요청이 들어왔습니다");
        //카테고리 이름이 없는 경우
        CategoryEntity category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(()->new NoSuchElementException("해당 카테고리가 없습니다"));

        //카테고리 이름 일치하는 칼럼 조회
        List<ProductEntity> targetProductsEntitys = productRepository.findByCategory_CategoryName(categoryName);

        //일치하는 리스트 넣을 리스트
        List<GetProductResponseDto> responseProductDtos = new ArrayList<>();

        //카테고리에 상품이 있는 경우
        if(targetProductsEntitys != null){
            for (ProductEntity productEntity : targetProductsEntitys) {
                if(productEntity.isSellable()){
                    GetProductResponseDto product =  GetProductResponseDto.toDto(productEntity);
                    responseProductDtos.add(product);
                    log.info("[Product] 카테고리 조회 리스트에 추가된 상품 : " + productEntity.getProductName());
                }
            }
        }

        if(responseProductDtos.isEmpty()) {
            log.info("[GetProduct] 해당 카테고리에 상품이 없습니다");
            return GetProductByCategoryReponseDto.builder()
                    .success(false)
                    .code(404)
                    .message("조회할 상품이 존재하지 않습니다.")
                    .build();
        }else{
            return GetProductByCategoryReponseDto.builder()
                    .success(true)
                    .code(200)
                    .message(categoryName+"상품을 조회하였습니다")
                    .build();
        }
    }
}
