package com.silenteight.universaldatasource.app.category.adapter.incoming.v1;

import lombok.RequiredArgsConstructor;

import com.silenteight.universaldatasource.app.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class CategoryAdapterConfiguration {

  private final ListAvailableCategoriesUseCase listAvailableCategoriesUseCase;

  private final GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase;

  private final CategoryVersionMapper categoryVersionMapper;

  @Bean(name = "categoryAdapterV1")
  CategoryAdapter categoryAdapter() {
    return new CategoryAdapter(listAvailableCategoriesUseCase, getMatchCategoryValuesUseCase,
        categoryVersionMapper);
  }
}
