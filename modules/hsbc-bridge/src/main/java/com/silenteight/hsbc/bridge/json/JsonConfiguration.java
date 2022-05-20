package com.silenteight.hsbc.bridge.json;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ JsonConfigurationProperties.class })
class JsonConfiguration {

  private final JsonConfigurationProperties properties;

  @Bean
  ObjectMapperJsonConverter jsonConverter() {
    return new ObjectMapperJsonConverter(properties.getAlertLimit());
  }
}
