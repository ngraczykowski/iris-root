package com.silenteight.serp.governance.policy.details;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyDetailsConfiguration {

  @Bean
  PolicyDetailsUseCase policyDetailsUseCase(
      PolicyDetailsRequestQuery policyDetailsRequestQuery,
      PolicyStepsCountQuery policyStepsCountQuery) {

    return new PolicyDetailsUseCase(policyDetailsRequestQuery, policyStepsCountQuery);
  }
}
