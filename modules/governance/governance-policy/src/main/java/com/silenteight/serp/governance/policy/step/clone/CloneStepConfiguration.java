package com.silenteight.serp.governance.policy.step.clone;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CloneStepConfiguration {

  @Bean
  CloneStepUseCase cloneStepUseCase(PolicyService policyService) {
    return new CloneStepUseCase(policyService);
  }
}
