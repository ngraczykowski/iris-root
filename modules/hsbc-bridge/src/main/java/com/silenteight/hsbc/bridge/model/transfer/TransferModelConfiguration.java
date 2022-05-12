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
        modelTransferModelLoader,
        repositoryClient);
  }

  @Bean
  HistoricalDecisionsModelManager historicalDecisionsModelManager(
      ModelRepository modelRepository,
      StoreModelUseCase storeModelUseCase,
      GetModelUseCase getModelUseCase,
      HistoricalDecisionsMessageSender historicalDecisionsMessageSender) {
    return new HistoricalDecisionsModelManager(
        jenkinsModelClient,
        modelRepository,
        getModelUseCase,
        storeModelUseCase,
        modelTransferModelLoader,
        historicalDecisionsMessageSender,
        repositoryClient);
  }

  @Bean
  GovernanceModelManager governanceModelManager() {
    return new GovernanceModelManager(
        jenkinsModelClient,
        repositoryClient,
        modelServiceClient);
  }
}
