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

    @Column(name = "option_stock")
    @Schema(description = "상품 옵션 별 재고",example = "1")
    private Integer optionStock;

}
