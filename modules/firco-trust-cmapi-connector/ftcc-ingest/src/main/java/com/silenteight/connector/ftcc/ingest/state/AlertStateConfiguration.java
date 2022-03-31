package com.silenteight.connector.ftcc.ingest.state;

import lombok.NonNull;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AlertStateProperties.class)
class AlertStateConfiguration {

  @Bean
  AlertStateMapper alertStateMapper(@NonNull AlertStateProperties properties) {
    return new AlertStateMapper(properties.stateByStatus());
  }
}
