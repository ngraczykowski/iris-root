package com.silenteight.serp.governance.changerequest.approve;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApproveChangeRequestConfiguration {

  @Bean
  ApproveChangeRequestUseCase approveChangeRequestUseCase(
      ChangeRequestService changeRequestService) {

    return new ApproveChangeRequestUseCase(changeRequestService);
  }
}
