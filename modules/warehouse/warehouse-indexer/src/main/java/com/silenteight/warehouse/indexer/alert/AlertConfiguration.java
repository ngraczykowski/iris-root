package com.silenteight.warehouse.indexer.alert;

import com.silenteight.sep.base.common.time.TimeSource;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class AlertConfiguration {

  @Bean
  AlertService alertService(
      RestHighLevelClient restHighLevelClient, TimeSource timeSource,
      @Valid ElasticsearchProperties elasticsearchProperties) {
    AlertMapper alertMapper = new AlertMapper(timeSource);
    return new AlertService(restHighLevelClient, alertMapper, elasticsearchProperties);
  }
}
