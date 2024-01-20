package com.github.backendpart.service;

import com.github.backendpart.repository.CategoryRepository;
import com.github.backendpart.repository.ProductRepository;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.addProduct.AddProductRequestDto;
import com.github.backendpart.web.dto.product.OptionDto;
import com.github.backendpart.web.dto.product.ProductDto;
import com.github.backendpart.web.dto.product.ProductImageDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductDetailResponseDto;
import com.github.backendpart.web.dto.product.getProduct.GetProductResponseDto;
import com.github.backendpart.web.entity.CategoryEntity;
import com.github.backendpart.web.entity.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final ImageUploadService imageUploadService;
    private final OptionService optionService;
    
    public GetProductDetailResponseDto findById(Long productId){
        ProductEntity targetProduct = productRepository.findById(productId).orElse(null);
        GetProductDetailResponseDto targetProductDto = GetProductDetailResponseDto.toDto(targetProduct);

        return targetProductDto;
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
                System.out.println(productEntity.getProductName());
                targetProductsDto.add(GetProductResponseDto.toDto(productEntity));
            }
        }

        return targetProductsDto;
    }
    
    
    public CommonResponseDto addProduct(AddProductRequestDto addProductRequestDto, List<MultipartFile> images){
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

            // 생성된 product에 이미지 추가
            if(images != null) {
                List<ProductImageDto> uploadedImages = imageUploadService.uploadImages(images);
                log.info("[addProduct] 상품에 이미지가 추가되었습니다. uploadedImages = " + uploadedImages);
            }

            // 생성된 product에 option 추가
            if(addProductRequestDto.getOptions() != null){
                List<OptionDto> addedOptions = optionService.addOption(addProductRequestDto.getOptions());
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
