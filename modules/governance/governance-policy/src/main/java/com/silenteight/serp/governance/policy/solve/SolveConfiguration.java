package com.silenteight.serp.governance.policy.solve;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.policy.solve.amqp.FeatureVectorSolvedMessageGateway;
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
      FeatureVectorSolvedMessageGateway gateway,
      CanonicalFeatureVectorFactory canonicalFeatureVectorFactory) {

    return new SolveUseCase(stepsConfigurationProvider, solvingService, gateway,
        canonicalFeatureVectorFactory, DefaultTimeSource.INSTANCE);
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
