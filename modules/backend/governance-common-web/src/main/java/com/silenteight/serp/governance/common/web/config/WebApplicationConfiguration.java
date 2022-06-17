package com.silenteight.serp.governance.common.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class WebApplicationConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "serp.governance")
  WebApplicationProperties webApplicationProperties() {
    return new WebApplicationProperties();
  }
}
