package com.silenteight.warehouse.indexer.alert.indexing;

import com.silenteight.warehouse.indexer.alert.mapping.AlertMappingProperties;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import javax.validation.Valid;

@Configuration
@EnableRetry
@EnableConfigurationProperties({
    ElasticsearchProperties.class,
    AlertMappingProperties.class
})
public class AlertConfiguration {

  @Bean
  AlertIndexService alertIndexService(
      RestHighLevelClient restHighLevelAdminClient,
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new AlertIndexService(
        restHighLevelAdminClient,
        elasticsearchProperties.getUpdateRequestBatchSize(),
        elasticsearchProperties.getRetryOnConflictCount());
  }
}
