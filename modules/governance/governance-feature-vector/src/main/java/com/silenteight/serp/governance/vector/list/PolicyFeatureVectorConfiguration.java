package com.silenteight.serp.governance.vector.list;

import com.silenteight.serp.governance.policy.domain.PolicyByIdQuery;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.solve.StepsSupplierProvider;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PolicyFeatureVectorConfiguration {

  @Bean
  FindMatchingFeatureVectorsUseCase findMatchingFeatureVectorsUseCase(
      FeatureVectorUsageQuery featureVectorUsageQuery,
      FeatureNamesQuery featureNamesQuery,
      PolicyStepsRequestQuery policyStepsRequestQuery,
      PolicyByIdQuery policyByIdQuery,
      StepsSupplierProvider stepsSupplierProvider) {

    return new FindMatchingFeatureVectorsUseCase(
        featureNamesQuery,
        new SolvingService(),
        featureVectorUsageQuery,
        policyStepsRequestQuery,
        policyByIdQuery,
        stepsSupplierProvider);
  }
}
