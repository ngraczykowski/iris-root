package com.silenteight.scb.feeding.infrastructure.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.feeding.domain.category.CategoriesRegistrationService;
import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CategoriesProperties.class)
public class CategoriesRegistrationServiceConfiguration {

  private final UniversalDatasourceApiClient universalDatasourceApiClient;

  @Bean
  CategoriesRegistrationService categoriesRegistrationService(
      CategoriesProperties categoriesProperties) {
    return new CategoriesRegistrationService(
        universalDatasourceApiClient, categoriesProperties.categories());
  }
}
