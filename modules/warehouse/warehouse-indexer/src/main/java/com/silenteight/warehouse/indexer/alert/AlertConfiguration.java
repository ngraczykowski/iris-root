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
  AlertIndexService alertIndexService(
      RestHighLevelClient restHighLevelAdminClient,
      @Valid AlertMappingProperties alertMappingProperties,
      @Valid ElasticsearchProperties elasticsearchProperties,
      TimeSource timeSource) {

    SkipAnyMatchingKeysStrategy ignoredKeysStrategy =
        new SkipAnyMatchingKeysStrategy(alertMappingProperties.getIgnoredKeys());

    AlertMapper alertMapper =
        new AlertMapper(timeSource, alertMappingProperties, ignoredKeysStrategy);

    return new AlertIndexService(
        restHighLevelAdminClient,
        alertMapper,
        elasticsearchProperties.getUpdateRequestBatchSize());
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

  @Bean
  AlertCopyDataService alertCopyDataService(
      RestHighLevelClient restHighLevelAdminClient,
      AlertSearchService alertSearchService,
      ProductionSearchRequestBuilder productionSearchRequestBuilder) {

    return new AlertCopyDataService(
        restHighLevelAdminClient,
        alertSearchService,
        productionSearchRequestBuilder);
  }
}
