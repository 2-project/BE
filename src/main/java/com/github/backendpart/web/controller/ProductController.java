package com.github.backendpart.web.controller;

import com.github.backendpart.service.ProductService;
import com.github.backendpart.web.dto.product.ProductDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductDetailResponseDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.addProduct.AddProductRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "상품 관리 API", description = "상품 추가 및 조회를 담당하는 api입니다.")
public class ProductController {
    private final ProductService productService;
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


    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "상품 추가 요청", description = "스웨거에서 테스트를 진행할 떄에는 productInfo도 json파일로 생성해서 테스트 진행해 주셔야합니다.")
    public ResponseEntity<CommonResponseDto> addProduct(
            @RequestPart(name = "productInfo") @Parameter(schema =@Schema(type = "string", format = "binary")) AddProductRequestDto addProductRequestDto,
            @RequestPart(name = "productImages", required = false) List<MultipartFile> productImages
            ){
        log.info("[POST](productController) 새로운 상품 추가 요청이 들어왔습니다. product = " + addProductRequestDto);
        CommonResponseDto addProductResult = productService.addProduct(addProductRequestDto, productImages);
        log.info(("[POST] addProduct결과 = " + addProductResult));

        return ResponseEntity.ok().body(addProductResult);
    }

    @DeleteMapping("/deleteProduct")
    @Operation(summary = "상품 삭제 요청")
    public ResponseEntity<CommonResponseDto> deleteProduct(
            @Schema(description = "상품 cid 리스트", example = "[ 1, 2, 4 ]")
            @RequestBody List<Long> productCidList
    ){
        log.info("[DELETE] 상품 삭제 요청이 들어왔습니다.");
        CommonResponseDto deleteProductResult = productService.deleteProduct(productCidList);
        log.info("[DELETE] deleteProduct결과 = " + deleteProductResult);

        return ResponseEntity.ok().body(deleteProductResult);
    }
}
