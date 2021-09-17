package com.silenteight.warehouse.indexer.alert;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;

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
