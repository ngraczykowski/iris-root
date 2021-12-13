package com.silenteight.payments.bridge.app.metrics.efficiency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EfficiencyMetricsConfiguration {

  @Bean
  EfficiencyMetricsMeter efficiencyMetricsMeter() {
    return new EfficiencyMetricsMeter();
  }

}
