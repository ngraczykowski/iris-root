package com.silenteight.warehouse.common.opendistro.elastic;

import com.silenteight.warehouse.common.opendistro.configuration.OpendistroProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.LOWER_CAMEL_CASE;

@Configuration
class OpendistroElasticConfiguration {

  @Bean
  OpendistroElasticClient opendistroElasticClient(
      RestHighLevelClient restHighLevelAdminClient,
      @Valid OpendistroProperties opendistroProperties) {

    ObjectMapper objectMapper = new ObjectMapper()
        .setPropertyNamingStrategy(LOWER_CAMEL_CASE)
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    return new OpendistroElasticClient(
        restHighLevelAdminClient.getLowLevelClient(),
        objectMapper,
        opendistroProperties.getMaxObjectCount());
  }
}
