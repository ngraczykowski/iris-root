package com.silenteight.warehouse.common.opendistro.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpendistroElasticConfiguration {

  @Bean
  OpendistroElasticClient opendistroElasticClient(
      RestClient restLowLevelClient, ObjectMapper objectMapper) {
    return new OpendistroElasticClient(restLowLevelClient, objectMapper);
  }
}
