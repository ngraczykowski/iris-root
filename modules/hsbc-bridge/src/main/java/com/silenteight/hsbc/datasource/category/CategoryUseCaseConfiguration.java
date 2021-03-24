package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class CategoryUseCaseConfiguration {

  private final CategoryRepository categoryRepository;
  private final MatchCategoryRepository matchCategoryRepository;

  @Bean
  ListCategoriesUseCase listCategoriesUseCase() {
    return new ListCategoriesUseCase(categoryRepository);
  }

  @Bean
  GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase() {
    return new GetMatchCategoryValuesUseCase(matchCategoryRepository);
  }
}
