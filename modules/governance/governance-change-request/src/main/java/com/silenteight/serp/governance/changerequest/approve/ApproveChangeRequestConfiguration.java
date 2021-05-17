package com.silenteight.serp.governance.changerequest.approve;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApproveChangeRequestConfiguration {

  @Bean
  ApproveChangeRequestUseCase approveChangeRequestUseCase(
      ChangeRequestService changeRequestService,
      ChangeRequestModelQuery changeRequestModelQuery,
      ApplicationEventPublisher eventPublisher) {

    return new ApproveChangeRequestUseCase(
        changeRequestService, changeRequestModelQuery, eventPublisher);
  }
}
