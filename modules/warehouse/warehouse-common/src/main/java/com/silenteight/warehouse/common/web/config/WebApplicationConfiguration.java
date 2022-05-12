package com.silenteight.warehouse.common.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class WebApplicationConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "warehouse.web")
  WebApplicationProperties webApplicationProperties() {
    return new WebApplicationProperties();
  }
}
