package com.silenteight.simulator.processing.alert.index.ack;

import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AckMessageConfiguration {

  @Bean
  FetchAckMessageUseCase fetchAckMessageUseCase(IndexedAlertService indexedAlertService) {
    return new FetchAckMessageUseCase(indexedAlertService);
  }
}
