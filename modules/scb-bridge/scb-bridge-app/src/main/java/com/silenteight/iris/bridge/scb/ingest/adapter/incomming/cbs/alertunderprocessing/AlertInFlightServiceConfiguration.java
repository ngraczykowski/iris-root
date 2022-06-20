/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AlertUnderProcessingProperties.class)
class AlertInFlightServiceConfiguration {

  private final AlertUnderProcessingRepository alertUnderProcessingRepository;
  private final AlertUnderProcessingProperties alertUnderProcessingProperties;

  @Bean
  AlertsUnderProcessingService alertInFlightService() {
    return new AlertsUnderProcessingService(
        alertUnderProcessingRepository, alertUnderProcessingProperties);
  }

}
