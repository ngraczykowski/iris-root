package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
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
      PolicyStepsRequestQuery stepQuery,
      PolicyStepsConfigurationQuery stepsConfigurationQuery,
      ReconfigurableStepsConfigurationFactory stepsConfigurationFactory,
      FeatureVectorService featureVectorService,
      SolvingService solvingService) {

    return new FindMatchingFeatureVectorsUseCase(
        stepQuery,
        stepsConfigurationQuery,
        stepsConfigurationFactory,
        featureVectorService,
        solvingService);
  }
}
