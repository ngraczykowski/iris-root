package com.silenteight.serp.governance.model.provide;

import com.silenteight.serp.governance.policy.step.logic.PolicyStepsMatchConditionsNamesProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProvideConfiguration {
  @Bean
  SolvingModelProvider solvingModelProvider(
      CurrentStrategyProvider currentStrategyProvider,
      PolicyFeatureProvider policyFeatureProvider,
      PolicyStepsMatchConditionsNamesProvider policyStepsConditionsProvider) {

    return new SolvingModelProvider(
        currentStrategyProvider,
        policyFeatureProvider,
        policyStepsConditionsProvider);
  }
}
