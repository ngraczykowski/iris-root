package com.silenteight.serp.governance.policy.step.edit;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EditStepConfiguration {

  @Bean
  SetPolicyStepsUseCase setPolicyStepsUseCase(PolicyService policyService) {
    return new SetPolicyStepsUseCase(policyService);
  }
}
