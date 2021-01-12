package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolveConfiguration {

  @Bean
  SolveUseCase solveUseCase(SolvingService solvingService) {
    return new SolveUseCase(solvingService);
  }

  @Bean
  SolvingService solvingService(StepPolicyFactory stepPolicyFactory) {
    return new SolvingService(stepPolicyFactory);
  }

  @Bean
  PolicyPromotedEventHandler policyPromotedEventHandler(
      PolicyService policyService, StepPolicyFactory stepPolicyFactory) {

    return new PolicyPromotedEventHandler(policyService, stepPolicyFactory);
  }

  @Bean
  StepPolicyFactory stepPolicyFactory() {
    return new StepPolicyFactory();
  }
}
