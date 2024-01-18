package com.github.backendpart.web.controller;

import com.github.backendpart.service.ProductService;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.addProduct.AddProductRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<CommonResponseDto> addProduct(
            @RequestPart(name = "productInfo") AddProductRequestDto addProductRequestDto,
            @RequestPart(name = "productImages", required = false) List<MultipartFile> productImages
            ){
        log.info("[Post](productController) 새로운 상품 추가 요청이 들어왔습니다. product = " + addProductRequestDto);
        CommonResponseDto result = productService.addProduct(addProductRequestDto, productImages);
        log.info(("[POST] addProduct결과 = " + result));

        return ResponseEntity.ok().body(result);
    }
}
