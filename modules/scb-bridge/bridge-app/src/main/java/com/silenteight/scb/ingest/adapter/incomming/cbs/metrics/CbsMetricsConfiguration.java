package com.silenteight.scb.ingest.adapter.incomming.cbs.metrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CbsMetricsConfiguration {

  @Bean
  CbsMetrics cbsMetrics() {
    return new CbsMetrics();
  }
}
