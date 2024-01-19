package com.github.backendpart.web.dto.product.addProduct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OptionRequestDto {
    @Schema(description = "옵션 종류", example = "XL")
    private String optionName;

    @Schema(description = "상품 옵션 별 재고", example = "37")
    private Integer optionStock;
}
