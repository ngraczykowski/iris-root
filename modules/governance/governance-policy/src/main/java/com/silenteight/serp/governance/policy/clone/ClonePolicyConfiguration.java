package com.silenteight.serp.governance.policy.clone;

import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ClonePolicyConfiguration {

  @Bean
  ClonePolicyUseCase clonePolicyUseCase(@NonNull PolicyService policyService) {
    return new ClonePolicyUseCase(policyService);
  }
}
