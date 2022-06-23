package com.silenteight.serp.governance.changerequest.create;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateChangeRequestConfiguration {

  @Bean
  CreateChangeRequestUseCase createChangeRequestUseCase(ChangeRequestService changeRequestService) {
    return new CreateChangeRequestUseCase(changeRequestService);
  }
}
