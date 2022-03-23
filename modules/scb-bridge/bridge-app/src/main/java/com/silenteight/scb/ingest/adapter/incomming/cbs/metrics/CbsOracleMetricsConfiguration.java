package com.silenteight.scb.ingest.adapter.incomming.cbs.metrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CbsOracleMetricsConfiguration {

  @Bean
  CbsOracleMetrics cbsOracleMetrics() {
    return new CbsOracleMetrics();
  }
}
