package com.silenteight.adjudication.engine.mock.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mock-datasource")
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
