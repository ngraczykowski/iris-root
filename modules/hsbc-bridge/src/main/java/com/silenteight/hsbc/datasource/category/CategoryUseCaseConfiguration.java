package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class CategoryUseCaseConfiguration {

  private final CategoryRepository categoryRepository;
  private final MatchCategoryRepository matchCategoryRepository;
  private final CategoryModelHolder categoryModelHolder;

  @Bean
  ListCategoriesUseCase listCategoriesUseCase() {
    return new ListCategoriesUseCase(categoryModelHolder);
  }

  @Bean
  GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase() {
    return new GetMatchCategoryValuesUseCase(matchCategoryRepository);
  }

  @Bean
  StoreMatchCategoriesUseCase storeMatchCategoriesUseCase(CategoryModelHolder categoryModelHolder) {
    return new StoreMatchCategoriesUseCase(
        categoryRepository, matchCategoryRepository, categoryModelHolder);
  }
}
