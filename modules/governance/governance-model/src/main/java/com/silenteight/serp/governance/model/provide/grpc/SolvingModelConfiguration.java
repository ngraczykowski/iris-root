package com.silenteight.serp.governance.model.provide.grpc;

import com.silenteight.serp.governance.model.category.CategoryRegistry;
import com.silenteight.serp.governance.model.transfer.importing.ImportModelUseCase;
import com.silenteight.serp.governance.policy.step.logic.PolicyStepsFeaturesProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolvingModelConfiguration {

  @Bean
  @GrpcService
  SolvingModelGrpcService modelGrpcService(
      DefaultModelQuery defaultModelQuery,
      SolvingModelDetailsQuery modelDetailsQuery,
      SolvingModelProvider solvingModelProvider,
      ImportModelUseCase importModelUseCase) {

    return new SolvingModelGrpcService(
        defaultModelQuery, modelDetailsQuery, solvingModelProvider, importModelUseCase);
  }

  @Bean
  SolvingModelProvider solvingModelProvider(
      CurrentStrategyProvider currentStrategyProvider,
      PolicyFeatureProvider policyFeatureProvider,
      CategoryRegistry categoryRegistry,
      PolicyStepsFeaturesProvider policyStepsFeaturesProvider) {

    return new SolvingModelProvider(
        currentStrategyProvider,
        policyFeatureProvider,
        categoryRegistry,
        policyStepsFeaturesProvider);
  }

}
