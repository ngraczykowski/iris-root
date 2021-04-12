package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class CategoryUseCaseConfiguration {

  private final MatchFacade matchFacade;
  private final CategoryRepository categoryRepository;
  private final MatchCategoryRepository matchCategoryRepository;

  @Bean
  ListCategoriesUseCase listCategoriesUseCase() {
    return new ListCategoriesUseCase(categoryRepository);
  }

  @Bean
  GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase() {
    return new GetMatchCategoryValuesUseCase(matchFacade, matchCategoryRepository);
  }

  @Bean
  StoreMatchCategoriesUseCase storeMatchCategoriesUseCase() {
    return new StoreMatchCategoriesUseCase(categoryRepository, matchCategoryRepository);
  }
}
