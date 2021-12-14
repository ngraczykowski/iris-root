package com.silenteight.payments.bridge.app.metrics;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class LearningMetricsConfiguration {

  @Bean
  LearningMetricsMeter learningMetricsMeter() {
    return new LearningMetricsMeter();
  }

}
