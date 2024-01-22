package com.github.backendpart.web.entity.dataLoader;

import com.github.backendpart.repository.CategoryRepository;
import com.github.backendpart.web.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CategoryDataLoaderIMPL implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        createDefaultCategories();
    }

    private void createDefaultCategories() {
        String[] defaultCategories = {"인기상품", "매거진", "아울렛", "주간특가"};

        for (String categoryName : defaultCategories){
            if (!categoryRepository.existsByCategoryName(categoryName)) {
                CategoryEntity category = CategoryEntity.builder()
                        .categoryName(categoryName)
                        .build();
                categoryRepository.save(category);
                log.info("[CreateDefaultCategories] 기본 카테고리가 생성되었습니다. 추가된 카테고리 = " + categoryName);
            }
        }
    }
}
