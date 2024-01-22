package com.github.backendpart.web.controller.product;

import com.github.backendpart.service.OptionService;
import com.github.backendpart.service.product.ProductService;
import com.github.backendpart.web.dto.product.getProduct.GetProductDetailResponseDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "상품 정보 API", description = "상품 조회를 담당하는 api입니다.")
public class ProductController {
    private final ProductService productService;
    private final OptionService optionService;
    @GetMapping("/productDetail/{productId}")
    @Operation(summary = "상품 상세 조회")
    public ResponseEntity<GetProductDetailResponseDto> getProductDetail(@PathVariable(name = "productId") Long productId){
        log.info("GET 상세 조회 요청이 들어왔습니다");
        GetProductDetailResponseDto productDto = productService.findById(productId);
        log.info("GET 상세 조회 요청 응답 값 = " + productDto);
        return ResponseEntity.ok().body(productDto);
    }

    @GetMapping("")
    @Operation(summary = "상품 카테고리별 조회")
    public ResponseEntity<List<GetProductResponseDto>> getProduct(@RequestParam(name="categoryName") String categoryName){
        log.info("GET 상품 카테고리별 조회 요청이 들어왔습니다");
        List<GetProductResponseDto> categoryProducts = productService.findByCategory(categoryName);

        log.info("GET 상품 카테고리별 조회 응답 값 = " + categoryProducts);
        return ResponseEntity.ok().body(categoryProducts);
    }

}
