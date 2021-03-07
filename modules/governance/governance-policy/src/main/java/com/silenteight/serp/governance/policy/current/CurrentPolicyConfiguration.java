package com.silenteight.serp.governance.policy.current;

import com.silenteight.serp.governance.policy.solve.InUsePolicyQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CurrentPolicyConfiguration {

  @Bean
  CurrentPolicyProvider currentPolicyProvider(InUsePolicyQuery inUsePolicyQuery) {
    return new CurrentPolicyProvider(inUsePolicyQuery);
  }
}
