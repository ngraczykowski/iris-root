package com.silenteight.serp.governance.model.provide.grpc;

import com.silenteight.serp.governance.model.transfer.importing.ImportModelUseCase;
import com.silenteight.serp.governance.model.use.UseModelUseCase;
import com.silenteight.serp.governance.policy.step.logic.PolicyStepsMatchConditionsNamesProvider;
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
      ImportModelUseCase importModelUseCase,
      UseModelUseCase useModelUseCase) {

    return new SolvingModelGrpcService(
        defaultModelQuery,
        modelDetailsQuery,
        solvingModelProvider,
        importModelUseCase,
        useModelUseCase);
  }

  @Bean
  SolvingModelProvider solvingModelProvider(
      CurrentStrategyProvider currentStrategyProvider,
      PolicyFeatureProvider policyFeatureProvider,
      PolicyStepsMatchConditionsNamesProvider policyStepsConditionsProvider) {

    return new SolvingModelProvider(
        currentStrategyProvider,
        policyFeatureProvider,
        policyStepsConditionsProvider);
  }
}
