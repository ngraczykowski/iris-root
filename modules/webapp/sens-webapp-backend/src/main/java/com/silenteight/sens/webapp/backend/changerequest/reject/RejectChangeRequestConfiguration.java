package com.silenteight.sens.webapp.backend.changerequest.reject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RejectChangeRequestConfiguration {

  @Bean
  RejectChangeRequestUseCase rejectChangeRequestUseCase() {
    return new RejectChangeRequestUseCase();
  }
}
