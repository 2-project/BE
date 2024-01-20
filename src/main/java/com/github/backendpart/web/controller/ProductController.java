package com.github.backendpart.web.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backendpart.service.OptionService;
import com.github.backendpart.service.ProductService;
import com.github.backendpart.web.dto.product.editProduct.EditProductRequestDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductDetailResponseDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.addProduct.AddProductRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PutMapping("/editOption")
    @Operation(summary = "상품 옵션별 재고 수정")
    public ResponseEntity<CommonResponseDto> editOptionStock(
            @RequestBody List<EditProductRequestDto> editProductRequestDtoList
    ){
        log.info("[PUT] 상품 옵션 재고 수량 요청이 들어왔습니다.");
        CommonResponseDto editProductResult = optionService.editOption(editProductRequestDtoList);
        log.info("[PUT] editProductResult결과 = " + editProductResult);

        return ResponseEntity.ok().body(editProductResult);
    }


    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<CommonResponseDto> notFoundExceptionHandler(NotFoundException e){
        return ResponseEntity.ok().body(CommonResponseDto.builder()
                .code(e.getStatusCode())
                .success(false)
                .message(e.getMessage())
                .build());
    }
}
