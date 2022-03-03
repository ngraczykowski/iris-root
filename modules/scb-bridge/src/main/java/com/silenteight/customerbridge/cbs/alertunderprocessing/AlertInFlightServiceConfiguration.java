package com.silenteight.customerbridge.cbs.alertunderprocessing;

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

  @Bean
  ReactiveAlertInFlightService reactiveAlertInFlightService() {
    return new ReactiveAlertInFlightService(alertInFlightService());
  }
}
