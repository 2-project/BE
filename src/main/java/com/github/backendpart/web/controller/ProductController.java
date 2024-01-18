package com.github.backendpart.web.controller;

import com.github.backendpart.service.ProductService;
import com.github.backendpart.web.dto.product.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("productDetail/{productId}")
    @Operation(summary = "상품 상세 조회")
    public ResponseEntity<ProductDto> getProductDetail(@PathVariable long productId){
        log.info("GET 상세 조회 요청이 들어왔습니다");
        ProductDto productDto = productService.findById(productId);
        log.info("GET 상세 조회 요청 응답 값 = "+productDto);
        return ResponseEntity.ok().body(productDto);
    }

    @GetMapping("")
    @Operation(summary = "상품 카테고리별 조회")
    public ResponseEntity<List<ProductDto>> getProduct(@RequestParam String categoryName){
        log.info("GET 상품 카테고리별 조회 요청이 들어왔습니다");
        List<ProductDto> categoryProducts = new ArrayList<>();
        //TODO 기능구현 해야함
        log.info("GET 상품 카테고리별 조회 응답 값 = " + categoryProducts);
        return ResponseEntity.ok().body(categoryProducts);
    }

}
