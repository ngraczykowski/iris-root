package com.silenteight.sens.webapp.backend.changerequest.approve;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApproveChangeRequestConfiguration {

  @Bean
  ApproveChangeRequestUseCase approveChangeRequestUseCase() {
    return new ApproveChangeRequestUseCase();
  }
}
