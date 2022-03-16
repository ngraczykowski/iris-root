package com.silenteight.payments.bridge.svb.learning.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.category.port.CreateCategoriesClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

import static org.apache.commons.collections4.MapUtils.emptyIfNull;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CreateCategoriesProperties.class)
class CreateCategoriesConfiguration {

  @Bean
  CreateCategoriesUseCase createCategoriesUseCase(
      final CreateCategoriesClient createCategoriesClient,
      @Valid final CreateCategoriesProperties properties) {
    return new CreateCategoriesUseCase(
        createCategoriesClient, emptyIfNull(properties.getSupported()));
  }
}
