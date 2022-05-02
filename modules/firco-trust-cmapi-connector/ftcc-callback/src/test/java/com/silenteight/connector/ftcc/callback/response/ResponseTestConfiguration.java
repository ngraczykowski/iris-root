package com.silenteight.connector.ftcc.callback.response;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

import static java.time.Clock.systemUTC;

@Configuration
class ResponseTestConfiguration {

  @Bean
  Clock clock() {
    return systemUTC();
  }
}
