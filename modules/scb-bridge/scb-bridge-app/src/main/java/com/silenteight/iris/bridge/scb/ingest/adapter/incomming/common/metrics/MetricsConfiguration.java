/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.metrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.context.annotation.AdviceMode.ASPECTJ;

@Configuration
@EnableAsync(mode = ASPECTJ)
class MetricsConfiguration {

  @Bean
  BridgeMetrics bridgeMetrics() {
    return new BridgeMetrics();
  }

  @Bean
  MeterFetchedAlertsListener alertsFetchedMeterListener(BridgeMetrics bridgeMetrics) {
    return new MeterFetchedAlertsListener(bridgeMetrics);
  }
}
