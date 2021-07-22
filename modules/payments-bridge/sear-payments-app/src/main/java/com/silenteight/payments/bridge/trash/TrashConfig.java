package com.silenteight.payments.bridge.trash;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TrashConfig {

  @Bean
  LogRequestAspect logRequestAspect() {
    return new LogRequestAspect();
  }

  @Bean
  CmapiAuthenticateAspect cmapiAuthenticate() {
    return new CmapiAuthenticateAspect();
  }

  @Bean
  AddDcToRequestDto addDcToRequestDto() {
    return new AddDcToRequestDto();
  }
}
