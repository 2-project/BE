package com.github.backendpart.web.entity;

import com.github.backendpart.web.dto.product.CategoryDto;
import com.github.backendpart.web.dto.product.OptionDto;
import com.github.backendpart.web.dto.product.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "option_table")
public class OptionEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_cid")
    private Long optionCid;

    @Column(name = "option_name")
    @Schema(description = "옵션 종류", example="S")
    private String optionName;

    @EmbeddedId
    private OptionPK optionPk;

    //TODO 어떤 상품의 어떤 옵션의 재고인지 파악 불가능하지 않나?
    @Column(name = "option_stock")
    @Schema(description = "상품 옵션 별 재고",example = "1")
    private Integer optionStock;

    public static OptionEntity toEntity(OptionDto optionDto) {
        return OptionEntity.builder()
                .optionCid(optionDto.getOptionCid())
                .optionName(optionDto.getOptionName())
                .optionStock(optionDto.getOptionStock())
                .build();
    }

}
