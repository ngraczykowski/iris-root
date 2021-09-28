package com.silenteight.warehouse.indexer.query.streaming;

import com.silenteight.warehouse.indexer.alert.ElasticsearchProperties;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({ ElasticsearchProperties.class, ScrollSearchProperties.class })
class ScrollSearchConfiguration {

  @Bean
  ScrollSearchStreamingService scrollSearchStreamingService(
      RestHighLevelClient restHighLevelUserAwareClient,
      @Valid ScrollSearchProperties scrollSearchProperties,
      ScrollSearchQueryBuilder queryBuilder,
      DataResponseParser parser) {

    return new ScrollSearchStreamingService(
        restHighLevelUserAwareClient,
        scrollSearchProperties,
        queryBuilder,
        parser);
  }

  @Bean
  DataResponseParser dataResponseParser() {
    return new DataResponseParser();
  }

  @Bean
  ScrollSearchQueryBuilder scrollSearchQueryBuilder() {
    return new ScrollSearchQueryBuilder();
  }
}
