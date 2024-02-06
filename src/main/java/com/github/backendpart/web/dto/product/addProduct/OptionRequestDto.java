package com.github.backendpart.web.dto.product.addProduct;

import com.github.backendpart.web.entity.OptionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class OptionRequestDto {
    @Schema(description = "옵션 종류", example = "XL")
    private String optionName;

    @Schema(description = "상품 옵션 별 재고", example = "37")
    private Integer optionStock;

    public static OptionRequestDto toDto(OptionEntity optionEntity){
        return OptionRequestDto.builder()
                .optionName(optionEntity.getOptionName())
                .optionStock(optionEntity.getOptionStock())
                .build();
    }
}
