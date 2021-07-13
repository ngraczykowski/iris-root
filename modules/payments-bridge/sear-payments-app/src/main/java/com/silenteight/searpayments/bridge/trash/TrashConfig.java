package com.silenteight.searpayments.bridge.trash;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TrashConfig {

  @Bean
  LogRequestAspect logRequestAspect(ApplicationContext applicationContext) {
    return new LogRequestAspect(applicationContext);
  }

  @Bean
  CmapiAuthenticateAspect cmapiAuthenticate(ApplicationContext applicationContext) {
    return new CmapiAuthenticateAspect(applicationContext);
  }

  @Bean
  AddDcToRequestDto addDcToRequestDto() {
    return new AddDcToRequestDto();
  }
}
