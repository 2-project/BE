package com.github.backendpart.service.product;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backendpart.repository.CategoryRepository;
import com.github.backendpart.repository.OptionRepository;
import com.github.backendpart.repository.ProductImageRepository;
import com.github.backendpart.repository.ProductRepository;
import com.github.backendpart.service.ImageUploadService;
import com.github.backendpart.service.OptionService;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.OptionDto;
import com.github.backendpart.web.dto.product.addProduct.AddProductRequestDto;
import com.github.backendpart.web.dto.product.adminGetProduct.AdminGetProductDetailDto;
import com.github.backendpart.web.dto.product.adminGetProduct.AdminGetProductResponseDto;
import com.github.backendpart.web.entity.CategoryEntity;
import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.ProductImageEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ProductImageRepository productImageRepository;

    private final ImageUploadService imageUploadService;
    private final OptionService optionService;

    @Transactional
    public CommonResponseDto addProduct(AddProductRequestDto addProductRequestDto, List<MultipartFile> images){
        try {
            log.info("[addProduct] 새로운 상품 추가 요청이 들어왔습니다. addProductRequestDto = " + addProductRequestDto);
            // 이미지와 옵션이 비어있는 product 생성
            CategoryEntity category = categoryRepository.findByCategoryName(addProductRequestDto.getCategory()).orElseThrow(()-> new NotFoundException("일치하는 카테고리가 존재하지 않습니다."));
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
                List<ProductImageEntity> uploadedImages = imageUploadService.uploadImages(images);
                newProductEntity.setProductImages(uploadedImages);
                productRepository.save(newProductEntity);
                log.info("[addProduct] 상품에 이미지가 추가되었습니다. uploadedImages = " + uploadedImages);
            }

            // 생성된 product에 option 추가
            if(addProductRequestDto.getOptions() != null){
                List<OptionEntity> addedOptions = optionService.addOption(addProductRequestDto.getOptions());
                newProductEntity.setOptions(addedOptions);
                productRepository.save(newProductEntity);
                log.info("[addProduct] 상품에 옵션이 추가되었습니다. addedOptions = " + addedOptions);
            }

            return CommonResponseDto.builder()
                    .code(200)
                    .message("상품추가에 성공하였습니다.")
                    .success(true)
                    .build();
        } catch (Exception e) {
            log.error("[ERROR] 에러가 났습니다. = " + e);
            throw new  NotFoundException("상품을 추가하지 못했습니다.");
        }
    }

    @Transactional
    public CommonResponseDto deleteProduct(List<Long> productCidList) {
            for(Long targetCid: productCidList){
                ProductEntity targetProduct = productRepository.findByProductCid(targetCid).orElseThrow(()->new NotFoundException("삭제할 상품이 존재하지 않습니다."));
                // s3 이미지 파일 삭제 로직 추가
                if(targetProduct.getProductImages().size() != 0){
                    for(ProductImageEntity targetImage : targetProduct.getProductImages()){
                        imageUploadService.deleteImage(targetImage.getProductImagePath());
                        productImageRepository.deleteById(targetImage.getProductImageCid());
                    }
                }
                // 옵션 삭제 로직 추가
                if(targetProduct.getOptions().size() != 0){
                    for(OptionEntity targetOption : targetProduct.getOptions()){
                        optionRepository.deleteById(targetOption.getOptionCid());
                    }
                }
                productRepository.deleteById(targetCid);
            }
            return CommonResponseDto.builder()
                    .code(200)
                    .message("상품 삭제가 완료되었습니다..")
                    .success(true)
                    .build();
    }


    public AdminGetProductResponseDto getAllProduct() {
        log.info("[GET_ALL_PRODUCT] 요청이 들어왔습니다.");
        List<ProductEntity> productList = productRepository.findAll();

        if(productList != null){
            log.info("[GET_ALL_PRODUCT] 조회를 완료했습니다. response타입으로 변경합니다.");
            List<AdminGetProductDetailDto> responseProducts = new ArrayList<>();
            for(ProductEntity product:productList){
                // optionDto 변경
                List<OptionDto> optionList = new ArrayList<>();
                for(OptionEntity option: product.getOptions()){
                    OptionDto optionDto = OptionDto.builder()
                            .optionCid(option.getOptionCid())
                            .optionStock(option.getOptionStock())
                            .optionName(option.getOptionName())
                            .build();
                    optionList.add(optionDto);
                }

                // productDto 변경
                AdminGetProductDetailDto productDetailDto = AdminGetProductDetailDto.builder()
                        .productCid(product.getProductCid())
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .productSaleStart(toSimpleDate(product.getProductSaleStart()))
                        .productSaleEnd(toSimpleDate(product.getProductSaleEnd()))
                        .options(optionList)
                        .category(product.getCategory().getCategoryName())
                        .build();

                responseProducts.add(productDetailDto);
            }
            return AdminGetProductResponseDto.builder()
                    .success(true)
                    .code(200)
                    .message("전체 상품을 조회하였습니다.")
                    .productDetailList(responseProducts)
                    .build();
        } else {
            log.info("[GET_ALL_PRODUCT] 상품이 존재하지 않습니다.");
            return AdminGetProductResponseDto.builder()
                    .success(false)
                    .code(404)
                    .message("조회할 상품이 존재하지 않습니다.")
                    .build();
        }
    }

    // simpleDate 타입 변환 로직
    public String toSimpleDate(Date date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
    }

}
