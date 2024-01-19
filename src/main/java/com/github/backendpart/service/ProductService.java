package com.github.backendpart.service;

import com.github.backendpart.repository.CategoryRepository;
import com.github.backendpart.repository.ProductRepository;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.addProduct.AddProductRequestDto;
import com.github.backendpart.web.dto.product.OptionDto;
import com.github.backendpart.web.dto.product.ProductDto;
import com.github.backendpart.web.dto.product.ProductImageDto;
import com.github.backendpart.web.entity.CategoryEntity;
import com.github.backendpart.web.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final ImageUploadService imageUploadService;
    private final OptionService optionService;
    
    public ProductDto findById(long productId){
        ProductEntity targetProduct = productRepository.findById(productId).orElse(null);
        ProductDto targetProductDto = ProductDto.toDto(targetProduct);
        return targetProductDto;
    }
    
    
    public CommonResponseDto addProduct(AddProductRequestDto addProductRequestDto, List<MultipartFile> images){
        //TODO
        // 1. 이미지와 옵션이 비어있는 product를 생성
        // 2. 생성된 product를 기반으로 product_cid를 받아옴
        // 3. product_cid와 옵션의 정보를 가지고 옵션을 추가
        // 4. product_cid와 이미지 정보를 가지고 이미지를 추가
        // 5. 최종적으로 결과 값 출력

        try {
            log.info("[addProduct] 새로운 상품 추가 요청이 들어왔습니다. addProductRequestDto = " + addProductRequestDto);
            // 이미지와 옵션이 비어있는 product 생성
            CategoryEntity category = categoryRepository.findByCategoryName(addProductRequestDto.getCategory());
            log.info("[category] 카테고리 결과 " + category.getCategoryName());
            ProductEntity newProductEntity = ProductEntity.builder()
                    .productName(addProductRequestDto.getProductName())
                    .productDescription(addProductRequestDto.getProductDescription())
                    .productPrice(addProductRequestDto.getProductPrice())
                    .productSaleStart(addProductRequestDto.getProductSaleStart())
                    .productSaleEnd(addProductRequestDto.getProductSaleEnd())
                    .category(category)
                    .build();
            productRepository.save(newProductEntity);
            log.info("[TEST] newProductEntity = " + newProductEntity);
            ProductDto newProductDto = ProductDto.toDto(newProductEntity);
            log.info("[addProduct] 새로운 상품이 추가되었습니다. newProductDto = " + newProductDto);

            // 생성된 product에 이미지 추가
            if(images != null) {
                List<ProductImageDto> uploadedImages = imageUploadService.uploadImages(images, newProductDto);
                log.info("[addProduct] 상품에 이미지가 추가되었습니다. uploadedImages = " + uploadedImages);
            }

            // 생성된 product에 option 추가
            if(addProductRequestDto.getOptions() != null){
                List<OptionDto> addedOptions = optionService.addOption(addProductRequestDto.getOptions(), newProductDto);
                log.info("[addProduct] 상품에 옵션이 추가되었습니다. addedOptions = " + addedOptions);
            }

            return CommonResponseDto.builder()
                    .code(200)
                    .message("상품추가에 성공하였습니다.")
                    .success(true)
                    .build();
        } catch (Exception e) {
            log.error("[ERROR] 에러가 났습니다. = " + e);
            return CommonResponseDto.builder()
                    .success(false)
                    .code(400)
                    .message("상품 업로드에 실패하였습니다.")
                    .build();
        }
    }

    public CommonResponseDto deleteProduct(List<Long> productCidList) {

        try{
            for(Long targetCid: productCidList){
            productRepository.deleteById(targetCid);
            }
            return CommonResponseDto.builder()
                    .code(200)
                    .message("상품 삭제가 완료되었습니다..")
                    .success(true)
                    .build();
        }catch (Exception e){
            log.error("[DELETE] 상품을 삭제하지 못하였습니다.");
            return CommonResponseDto.builder()
                    .code(400)
                    .message("상품이 존재하지 않습니다.")
                    .success(false)
                    .build();
        }
    }
}
