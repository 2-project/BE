package com.github.backendpart.web.dto.product;

import com.github.backendpart.web.entity.CategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class CategoryDto {
    @Schema(description = "카테고리 고유 아이디")
    private Long categoryCid;

    @Schema(description = "카테고리 이름", example = "인기상품")
    private String categoryName;

    public static CategoryDto toDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .categoryCid(categoryEntity.getCategoryCid())
                .categoryName(categoryEntity.getCategoryName())
                .build();
    }

    public static CategoryEntity toEntity(CategoryDto categoryDto){
        return CategoryEntity.builder()
                .categoryCid(categoryDto.getCategoryCid())
                .categoryName(categoryDto.getCategoryName())
                .build();
    }

}
