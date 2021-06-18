package com.silenteight.serp.governance.policy.solve;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.policy.domain.InUsePolicyQuery;
import com.silenteight.serp.governance.policy.solve.amqp.FeatureVectorSolvedMessageGateway;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolveConfiguration {

  @Bean
  PolicyStepsSupplierFactory stepsSupplierFactory(
      PolicyStepsConfigurationQuery stepsConfigurationQuery,
      InUsePolicyQuery inUsePolicyQuery) {
    return new PolicyStepsSupplierFactory(
        stepsConfigurationQuery, inUsePolicyQuery, new StepMapper());
  }

  @Bean
  SolveUseCase solveUseCase(
      StepsSupplierProvider stepsSupplierProvider,
      FeatureVectorSolvedMessageGateway gateway,
      CanonicalFeatureVectorFactory canonicalFeatureVectorFactory) {

    return new SolveUseCase(
        stepsSupplierProvider,
        new SolvingService(),
        gateway,
        canonicalFeatureVectorFactory,
        DefaultTimeSource.INSTANCE);
  }
}
