package com.silenteight.serp.governance.model.use;

import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UseModelConfiguration {

  @Bean
  UseModelUseCase useModelUseCase(
      ModelDetailsQuery modelDetailsQuery,
      PolicyDetailsQuery policyDetailsQuery,
      PolicyService policyService) {

    return new UseModelUseCase(modelDetailsQuery, policyDetailsQuery, policyService);
  }
}
