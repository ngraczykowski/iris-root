package com.silenteight.warehouse.indexer.query.single;

import com.silenteight.warehouse.indexer.alert.AlertMappingProperties;
import com.silenteight.warehouse.indexer.alert.ElasticsearchProperties;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({
    ElasticsearchProperties.class,
    AlertMappingProperties.class
})
public class SingleAlertQueryConfiguration {

  @Bean
  AlertSearchService searchAlertService() {
    return new AlertSearchService();
  }

  @Bean
  ProductionSearchRequestBuilder productionSearchRequestBuilder(
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new ProductionSearchRequestBuilder(elasticsearchProperties);
  }

  @Bean
  AlertQueryService alertQueryService(
      RestHighLevelClient restHighLevelUserAwareClient, AlertSearchService alertSearchService,
      ProductionSearchRequestBuilder productionSearchRequestBuilder) {

    return new AlertQueryService(restHighLevelUserAwareClient, alertSearchService,
        productionSearchRequestBuilder);
  }

  @Bean
  RandomAlertQueryService randomAlertQueryService(
      AlertSearchService alertSearchService, RestHighLevelClient restHighLevelAdminClient,
      ProductionSearchRequestBuilder productionSearchRequestBuilder) {

    return new RandomAlertQueryService(alertSearchService, restHighLevelAdminClient,
        productionSearchRequestBuilder);
  }
}
