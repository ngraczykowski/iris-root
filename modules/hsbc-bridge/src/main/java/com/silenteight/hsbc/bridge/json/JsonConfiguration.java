package com.silenteight.hsbc.bridge.json;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JsonConfiguration {

  @Bean
  ObjectMapperJsonConverter jsonConverter() {
    return new ObjectMapperJsonConverter();
  }
}
