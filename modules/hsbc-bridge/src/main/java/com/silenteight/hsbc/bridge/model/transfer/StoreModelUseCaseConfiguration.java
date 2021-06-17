package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class StoreModelUseCaseConfiguration {

  private final ModelInformationRepository modelRepository;

  @Bean
  StoreModelUseCase storeModelUseCase() {
    return new StoreModelUseCase(modelRepository);
  }
}
