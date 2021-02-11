package com.silenteight.serp.governance.policy.step.create;

import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateStepConfiguration {

  @Bean
  CreateStepUseCase createStepUseCase(@NonNull PolicyService policyService) {
    return new CreateStepUseCase(policyService);
  }
}
