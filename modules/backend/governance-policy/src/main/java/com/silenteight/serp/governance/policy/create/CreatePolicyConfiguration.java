package com.silenteight.serp.governance.policy.create;

import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreatePolicyConfiguration {

  @Bean
  CreatePolicyUseCase createPolicyUseCase(@NonNull PolicyService policyService) {
    return new CreatePolicyUseCase(policyService);
  }
}
