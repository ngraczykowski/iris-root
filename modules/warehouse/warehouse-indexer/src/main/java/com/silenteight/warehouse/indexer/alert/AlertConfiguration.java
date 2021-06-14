package com.silenteight.warehouse.indexer.alert;

import com.silenteight.sep.base.common.time.TimeSource;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
class AlertConfiguration {

  @Bean
  AlertService alertService(
      RestHighLevelClient restHighLevelAdminClient,
      TimeSource timeSource) {

    AlertMapper alertMapper = new AlertMapper(timeSource);
    return new AlertService(
        restHighLevelAdminClient,
        alertMapper);
  }

  @Bean
  AlertQueryService alertQueryService(
      RestHighLevelClient restHighLevelUserAwareClient,
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new AlertQueryService(
        restHighLevelUserAwareClient,
        elasticsearchProperties);
  }
}
