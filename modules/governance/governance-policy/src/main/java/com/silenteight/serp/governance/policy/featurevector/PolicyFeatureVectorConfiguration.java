package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.serp.governance.policy.solve.ReconfigurableStepsConfigurationFactory;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;
import com.silenteight.serp.governance.policy.step.PolicyStepsRequestQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PolicyFeatureVectorConfiguration {

  @Bean
  FindMatchingFeatureVectorsUseCase findMatchingFeatureVectorsUseCase(
      StepsConfigurationSupplierFactory stepsConfigurationSupplierFactory,
      FeatureVectorUsageQuery featureVectorUsageQuery,
      FeatureNamesQuery featureNamesQuery,
      SolvingService solvingService) {

    return new FindMatchingFeatureVectorsUseCase(
        stepsConfigurationSupplierFactory,
        featureVectorUsageQuery,
        featureNamesQuery,
        solvingService);
  }

  @Bean
  StepsConfigurationSupplierFactory stepsConfigurationSupplierFactory(
      PolicyStepsRequestQuery stepQuery,
      PolicyStepsConfigurationQuery stepsConfigurationQuery,
      ReconfigurableStepsConfigurationFactory stepsConfigurationFactory) {

    return new StepsConfigurationSupplierFactory(
        stepQuery, stepsConfigurationQuery, stepsConfigurationFactory);
  }
}
