package com.silenteight.adjudication.engine.mock.datasource.v2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mockdatasource & !datasourcev1")
class GrpcDatasourceApiMockConfiguration {

  @Bean
  MockListCategoriesUseCase mockCategoriesUseCase() {
    return new MockListCategoriesUseCase();
  }

  @Bean
  MockGetMatchCategoryValuesUseCase mockGetMatchCategoryValuesUseCase() {
    return new MockGetMatchCategoryValuesUseCase();
  }

  @Bean
  MockCommentsInputUseCase mockCommentsInputUseCase() {
    return new MockCommentsInputUseCase();
  }


}
