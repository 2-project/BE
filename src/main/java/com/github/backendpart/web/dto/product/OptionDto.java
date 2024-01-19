package com.github.backendpart.web.dto.product;

import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OptionDto {
    @Schema(description = "상품 옵션 고유 아이디")
    private Long optionCid;

    @Schema(description = "상품 Entity")
    private ProductDto product;

    @Schema(description = "옵션 종류", example = "S")
    private String optionName;

    @Schema(description = "상품 옵션 별 재고", example = "1")
    private Integer optionStock;


    public static OptionEntity toEntity(OptionDto optionDto) {
        return OptionEntity.builder()
                .optionName(optionDto.getOptionName())
                .optionStock(optionDto.getOptionStock())
                .product(ProductDto.toEntity(optionDto.getProduct()))
                .build();
    }

    public static OptionDto toDto(OptionEntity optionEntity){
        return OptionDto.builder()
                .optionCid(optionEntity.getOptionCid())
                .product(ProductDto.toDto(optionEntity.getProduct()))
                .optionName(optionEntity.getOptionName())
                .optionStock(optionEntity.getOptionStock())
                .build();
    }

}
