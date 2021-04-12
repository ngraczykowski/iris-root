package com.silenteight.warehouse.indexer.alert;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AlertConfiguration {

  @Bean
  AlertService alertService(RestHighLevelClient restHighLevelClient) {
    AlertMapper alertMapper = new AlertMapper();
    return new AlertService(restHighLevelClient, alertMapper);
  }
}
