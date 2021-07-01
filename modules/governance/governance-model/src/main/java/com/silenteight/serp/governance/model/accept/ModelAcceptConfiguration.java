package com.silenteight.serp.governance.model.accept;

import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.transfer.export.SendPromoteMessageUseCase;
import com.silenteight.serp.governance.policy.promote.PromotePolicyUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelAcceptConfiguration {

  @Bean
  ModelAcceptedEventListener modelAcceptedEventListener(
      ModelDetailsQuery modelDetailsQuery,
      PromotePolicyUseCase promotePolicyUseCase,
      SendPromoteMessageUseCase sendPromoteMessageUseCase) {

    return new ModelAcceptedEventListener(
        modelDetailsQuery, promotePolicyUseCase, sendPromoteMessageUseCase);
  }
}
