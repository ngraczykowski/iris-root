package com.silenteight.warehouse.retention.production.alert.erasing;


import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import javax.validation.Valid;

@Configuration
@EnableRetry
@EnableConfigurationProperties(DocumentEraseProperties.class)
class DocumentEraseConfiguration {

  @Bean
  DocumentEraseService documentEraseService(
      RestHighLevelClient restHighLevelAdminClient,
      @Valid DocumentEraseProperties elasticsearchProperties) {

    return new DocumentEraseService(
        restHighLevelAdminClient,
        elasticsearchProperties.getEraseRequestBatchSize());
  }
}
