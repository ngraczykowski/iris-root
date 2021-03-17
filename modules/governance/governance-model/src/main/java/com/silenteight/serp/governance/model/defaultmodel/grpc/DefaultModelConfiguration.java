package com.silenteight.serp.governance.model.defaultmodel.grpc;

import com.silenteight.serp.governance.model.category.CategoryRegistry;
import com.silenteight.serp.governance.model.featureset.CurrentFeatureSetProvider;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DefaultModelConfiguration {

  @Bean
  DefaultModelQuery defaultModelQuery(
      CurrentStrategyProvider currentStrategyProvider,
      CurrentPolicyProvider currentPolicyProvider,
      CurrentFeatureSetProvider currentFeatureSetProvider,
      CategoryRegistry categoryRegistry) {

    return new DefaultModelQuery(currentStrategyProvider, currentPolicyProvider,
        currentFeatureSetProvider, categoryRegistry);
  }
}
