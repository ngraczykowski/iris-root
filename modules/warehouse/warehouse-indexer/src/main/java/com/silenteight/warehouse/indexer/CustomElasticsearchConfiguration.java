package com.silenteight.warehouse.indexer;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
class CustomElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

  @Autowired
  private RestClientBuilder restClientBuilder;

  @Bean
  @Override
  public RestHighLevelClient elasticsearchClient() {
    return new RestHighLevelClient(restClientBuilder);
  }
}
