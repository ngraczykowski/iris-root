package com.silenteight.serp.governance.model.provide.grpc;

import com.silenteight.serp.governance.model.category.CategoryRegistry;
import com.silenteight.serp.governance.model.featureset.CurrentFeatureSetProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolvingModelProviderConfiguration {

  @Bean
  @GrpcService
  SolvingModelGrpcService modelGrpcService(
      DefaultModelQuery defaultModelQuery,
      SolvingModelDetailsQuery modelDetailsQuery,
      SolvingModelProvider solvingModelProvider) {
    return new SolvingModelGrpcService(defaultModelQuery, modelDetailsQuery, solvingModelProvider);
  }

  @Bean
  SolvingModelProvider solvingModelProvider(
      CurrentStrategyProvider currentStrategyProvider,
      CurrentFeatureSetProvider currentFeatureSetProvider,
      CategoryRegistry categoryRegistry) {
    return new SolvingModelProvider(
        currentStrategyProvider, currentFeatureSetProvider, categoryRegistry);
  }

}
