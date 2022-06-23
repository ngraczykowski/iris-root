package com.silenteight.serp.governance.policy.promote;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PromotePolicyConfiguration {

  @Bean
  PromotePolicyUseCase policyPromoteEventHandler(PolicyService policyService) {
    return new PromotePolicyUseCase(policyService);
  }
}
