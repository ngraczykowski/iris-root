package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertInFlightServiceConfiguration {

  private final AlertUnderProcessingRepository alertUnderProcessingRepository;

  @Bean
  AlertsUnderProcessingService alertInFlightService() {
    return new AlertsUnderProcessingService(alertUnderProcessingRepository);
  }

}
