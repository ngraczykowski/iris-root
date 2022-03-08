package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertIdPublisherConfiguration {

  private final AlertInFlightService alertInFlightService;

  @Bean
  AlertIdPublisher alertIdPublisher() {
    return AlertIdPublisher.builder()
        .alertInFlightService(alertInFlightService)
        .build();
  }
}
