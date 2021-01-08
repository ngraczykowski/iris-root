package com.silenteight.serp.governance.policy.solve;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
  StepPolicyFactory stepPolicyFactory() {
    return new StepPolicyFactory() {

      @Override
      public List<Step> getSteps() {
        return new ArrayList<>();
      }

      @Override
      public void reconfigure(List<Step> steps) {

      }
    };
  }
}
