package com.silenteight.serp.governance.model.used;

import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.provide.SolvingModelQuery;
import com.silenteight.serp.governance.model.used.amqp.ModelUsedOnProductionMessageGateway;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
class ModelUsedOnProductionConfiguration {

  @Bean
  MarkModelAsUsedOnProductionUseCase markModelAsUsedOnProductionUseCase(
      ModelDetailsQuery modelDetailsQuery,
      PolicyDetailsQuery policyDetailsQuery,
      PolicyService policyService,
      SendModelUsedOnProductionUseCase sendModelUsedOnProductionUseCase) {

    return new MarkModelAsUsedOnProductionUseCase(
        modelDetailsQuery, policyDetailsQuery, policyService, sendModelUsedOnProductionUseCase);
  }

  @DependsOn(ModelUsedOnProductionMessageGateway.ID)
  @Bean
  SendModelUsedOnProductionUseCase notifyModelUsedOnProductionUseCase(
      ModelUsedOnProductionMessageGateway modelUsedMessageGateway, SolvingModelQuery solvingModelQuery) {

    return new SendModelUsedOnProductionUseCase(modelUsedMessageGateway, solvingModelQuery);
  }
}
