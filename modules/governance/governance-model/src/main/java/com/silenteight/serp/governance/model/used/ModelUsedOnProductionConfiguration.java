package com.silenteight.serp.governance.model.used;

import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelUsedOnProductionConfiguration {

  @Bean
  MarkModelAsUsedOnProductionUseCase markModelAsUsedOnProductionUseCase(
      ModelDetailsQuery modelDetailsQuery,
      PolicyDetailsQuery policyDetailsQuery,
      PolicyService policyService) {

    return new MarkModelAsUsedOnProductionUseCase(
        modelDetailsQuery, policyDetailsQuery, policyService);
  }
}
