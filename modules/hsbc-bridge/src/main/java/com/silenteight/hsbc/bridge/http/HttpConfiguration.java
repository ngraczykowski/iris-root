package com.silenteight.hsbc.bridge.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HttpConfiguration {

  @Bean
  HttpAdapter httpAdapter() {
    return new HttpAdapter();
  }
}
