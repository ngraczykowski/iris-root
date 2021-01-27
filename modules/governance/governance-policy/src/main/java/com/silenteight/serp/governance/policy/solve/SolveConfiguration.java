package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.analytics.StoreFeatureVectorSolvedUseCase;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolveConfiguration {

  @Bean
  SolveUseCase solveUseCase(
      @Qualifier("inUsePolicyStepsSupplier") StepsConfigurationSupplier stepsConfigurationProvider,
      SolvingService solvingService,
      StoreFeatureVectorSolvedUseCase handler) {

    return new SolveUseCase(stepsConfigurationProvider, solvingService, handler);
  }

  @Bean
  SolvingService solvingService() {
    return new SolvingService();
  }

  StepMapper stepMapper() {
    return new StepMapper();
  }

  @Bean()
  InUsePolicyStepsSupplier inUsePolicyStepsSupplier(
      PolicyStepsConfigurationQuery stepsConfigurationQuery) {
    return new InUsePolicyStepsSupplier(stepsConfigurationQuery, stepMapper());
  }

  @Bean
  DefaultStepsConfigurationFactory defaultStepsConfigurationFactory() {
    return new DefaultStepsConfigurationFactory(stepMapper());
  }
}
