package com.silenteight.warehouse.indexer;

import com.silenteight.warehouse.indexer.gateway.IndexedConfirmationGateway;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IndexerIntegrationProperties.class)
class IndexerConfiguration {

  @Bean
  AlertIndexUseCase alertIndexUseCase(
      RestHighLevelClient restHighLevelClient,
      IndexedConfirmationGateway indexedConfirmationGateway) {

    return new AlertIndexUseCase(restHighLevelClient, indexedConfirmationGateway);
  }
}
