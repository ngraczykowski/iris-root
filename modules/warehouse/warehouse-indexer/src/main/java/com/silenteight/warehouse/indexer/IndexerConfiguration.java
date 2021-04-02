package com.silenteight.warehouse.indexer;

import com.silenteight.warehouse.indexer.gateway.IndexedConfirmationGateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IndexerIntegrationProperties.class)
class IndexerConfiguration {

  @Bean
  AlertIndexUseCase alertIndexUseCase(IndexedConfirmationGateway indexedConfirmationGateway) {
    return new AlertIndexUseCase(indexedConfirmationGateway);
  }
}
