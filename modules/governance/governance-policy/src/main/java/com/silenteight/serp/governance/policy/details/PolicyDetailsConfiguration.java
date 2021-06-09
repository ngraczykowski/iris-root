package com.silenteight.serp.governance.policy.details;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyDetailsConfiguration {

  @Bean
  PolicyDetailsUseCase policyDetailsUseCase(
      PolicyDetailsQuery policyDetailsQuery,
      PolicyStepsCountQuery policyStepsCountQuery) {

    return new PolicyDetailsUseCase(policyDetailsQuery, policyStepsCountQuery);
  }
}
