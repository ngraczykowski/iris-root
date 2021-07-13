package com.silenteight.searpayments.bridge.trash;

import org.springframework.context.ApplicationContext;
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
  com.silenteight.searpaymentsmockup.AddDcToRequestDto addDcToRequestDto() {
    return new com.silenteight.searpaymentsmockup.AddDcToRequestDto();
  }
}
