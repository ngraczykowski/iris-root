package com.silenteight.warehouse.indexer.alert;

import com.silenteight.sep.base.common.time.TimeSource;

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
public class AlertConfiguration {

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
  AlertService alertService(
      RestHighLevelClient restHighLevelAdminClient,
      @Valid AlertMappingProperties alertMappingProperties,
      TimeSource timeSource) {

    AlertMapper alertMapper = new AlertMapper(timeSource, alertMappingProperties);
    return new AlertService(
        restHighLevelAdminClient,
        alertMapper);
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
