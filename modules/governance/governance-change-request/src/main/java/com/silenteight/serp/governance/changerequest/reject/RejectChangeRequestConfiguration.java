package com.silenteight.serp.governance.changerequest.reject;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RejectChangeRequestConfiguration {

  @Bean
  RejectChangeRequestUseCase rejectChangeRequestUseCase(
      ChangeRequestService changeRequestService) {

    return new RejectChangeRequestUseCase(changeRequestService);
  }
}
