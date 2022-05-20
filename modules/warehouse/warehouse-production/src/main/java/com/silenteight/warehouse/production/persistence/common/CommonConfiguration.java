package com.silenteight.warehouse.production.persistence.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CommonConfiguration {

  @Bean
  PayloadConverter payloadConverter() {
    return new PayloadConverter(new ObjectMapper());
  }
}
