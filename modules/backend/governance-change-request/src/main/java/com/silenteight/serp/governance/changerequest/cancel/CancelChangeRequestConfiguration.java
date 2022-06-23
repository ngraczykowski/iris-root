package com.silenteight.serp.governance.changerequest.cancel;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CancelChangeRequestConfiguration {

  @Bean
  CancelChangeRequestUseCase cancelChangeRequestUseCase(
      ChangeRequestService changeRequestService) {

    return new CancelChangeRequestUseCase(changeRequestService);
  }
}
