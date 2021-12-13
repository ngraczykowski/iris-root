package com.silenteight.payments.bridge.app.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
class LearningMetricsConfiguration {

  @Bean
  LearningMetricsMeter solvingAlertsCounterMetrics() {
    return new LearningMetricsMeter();
  }

}
