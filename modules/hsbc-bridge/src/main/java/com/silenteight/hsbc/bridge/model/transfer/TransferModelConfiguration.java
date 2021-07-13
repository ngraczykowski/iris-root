package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class TransferModelConfiguration {

  private final ModelClient jenkinsModelClient;
  private final RepositoryClient repositoryClient;
  private final ModelServiceClient modelServiceClient;
  private final ModelTransferModelLoader modelTransferModelLoader;

  @Bean
  WorldCheckModelManager worldCheckModelManager(
      ModelRepository modelRepository,
      StoreModelUseCase storeModelUseCase,
      GetModelUseCase getModelUseCase,
      WorldCheckMessageSender worldCheckMessageSender) {
    return new WorldCheckModelManager(
        jenkinsModelClient,
        storeModelUseCase,
        getModelUseCase,
        worldCheckMessageSender,
        modelRepository,
        modelTransferModelLoader);
  }

  @Bean
  GovernanceModelManager governanceModelManager() {
    return new GovernanceModelManager(
        jenkinsModelClient,
        repositoryClient,
        modelServiceClient);
  }
}
