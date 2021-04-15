package com.silenteight.warehouse.common.testing.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TestingElasticsearchConfiguration {

  @Bean
  SimpleElasticTestClient simpleElasticTestClient(RestHighLevelClient restHighLevelClient) {
    return new SimpleElasticTestClient(restHighLevelClient);
  }
}
