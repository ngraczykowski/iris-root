package com.silenteight.serp.governance.model.accept;

import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.promote.PromotePolicyUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelAcceptConfiguration {

  @Bean
  ModelAcceptedEventListener modelAcceptedEventListener(
      ModelDetailsQuery modelDetailsQuery, PromotePolicyUseCase promotePolicyUseCase) {

    return new ModelAcceptedEventListener(modelDetailsQuery, promotePolicyUseCase);
  }
}
