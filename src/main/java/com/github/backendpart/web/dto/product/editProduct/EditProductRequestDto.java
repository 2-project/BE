package com.github.backendpart.web.dto.product.editProduct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EditProductRequestDto {
    @Schema(description = "변경될 옵션 cid", example = "1")
    private Long optionCid;

    @Schema(description = "변경될 옵션 재고", example = "144")
    private Integer optionStock;
}
