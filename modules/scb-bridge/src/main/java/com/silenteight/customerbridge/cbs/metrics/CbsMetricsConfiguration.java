package com.silenteight.customerbridge.cbs.metrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CbsMetricsConfiguration {

  @Bean
  CbsMetrics cbsMetrics() {
    return new CbsMetrics();
  }
}
