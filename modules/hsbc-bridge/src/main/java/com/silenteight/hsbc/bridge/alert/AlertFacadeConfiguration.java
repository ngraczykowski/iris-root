package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertFacadeConfiguration {

  @Bean
  AlertFacade alertFacade() {
    return new AlertFacade(new AlertMapper());
  }
}
