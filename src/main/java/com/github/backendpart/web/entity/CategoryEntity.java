package com.github.backendpart.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "category_table")
public class CategoryEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_cid")
    @Schema(description = "카테고리 고유 아이디")
    private Long categoryCid;

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> products;

    @Column(name = "category_name")
    @Schema(description = "카테고리 이름",example = "인기상품")
    private String categoryName;

}
