/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.infrastructure.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient;
import com.silenteight.iris.bridge.scb.feeding.domain.category.CategoriesRegistrationService;

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
