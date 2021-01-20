package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolveConfiguration {

  @Bean
  SolveUseCase solveUseCase(StepPolicyFactory stepPolicyFactory, SolvingService solvingService) {
    return new SolveUseCase(stepPolicyFactory, solvingService);
  }

  @Bean
  SolvingService solvingService() {
    return new SolvingService();
  }

  @Bean
  PolicyPromotedEventHandler policyPromotedEventHandler(
      PolicyStepsConfigurationQuery stepsConfigurationQuery, StepPolicyFactory stepPolicyFactory) {

    return new PolicyPromotedEventHandler(stepsConfigurationQuery, stepPolicyFactory);
  }

  @Bean
  StepPolicyFactory stepPolicyFactory() {
    return new StepPolicyFactory();
  }
}
