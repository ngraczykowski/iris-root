package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = { CategoriesProperties.class })
@RequiredArgsConstructor
public class CategoryUpdaterConfiguration {

  private final CategoryRepository categoryRepository;

  @Bean
  CategoryModelHolder getCategoryModelHolder(CategoriesProperties categoriesProperties) {
    return new CategoryModelHolder(categoriesProperties.getRiskTypeValues());
  }

  @Bean
  CategoryUpdater getCategoryUpdater(CategoryModelHolder categoryModelHolder) {
    return new CategoryUpdater(categoryRepository, categoryModelHolder);
  }
}
