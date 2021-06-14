package com.silenteight.warehouse.common.testing.elasticsearch;

import com.silenteight.warehouse.common.elastic.ElasticsearchClientConfiguration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ElasticsearchClientConfiguration.class)
class TestingElasticsearchConfiguration {

  @Bean
  SimpleElasticTestClient simpleElasticTestClient(
      RestHighLevelClient restHighLevelAdminClient) {

    return new SimpleElasticTestClient(restHighLevelAdminClient);
  }
}
