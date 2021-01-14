package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

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
      PolicyStepsConfigurationQuery stepsConfigurationQuery, StepPolicyFactory stepPolicyFactory) {

    return new PolicyPromotedEventHandler(stepsConfigurationQuery, stepPolicyFactory);
  }

  @Bean
  StepPolicyFactory stepPolicyFactory() {
    return new StepPolicyFactory();
  }
}
