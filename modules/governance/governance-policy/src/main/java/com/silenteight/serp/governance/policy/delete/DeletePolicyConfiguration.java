package com.silenteight.serp.governance.policy.delete;

import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeletePolicyConfiguration {

  @Bean
  DeletePolicyUseCase deletePolicyUseCase(@NonNull PolicyService policyService) {
    return new DeletePolicyUseCase(policyService);
  }
}
