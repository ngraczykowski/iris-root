package com.silenteight.payments.bridge.app.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.metrics.learning.SolvingMetricsIncrementerPort;

import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
class SolvingMetricsIncrementer implements SolvingMetricsIncrementerPort {

  private final LearningMetricsMeter learningMetricsMeter;
  private static final String TYPE_SOLVING = "solving";

  @Override
  public void increment(double value) {
    log.debug("Increment metrics counter solving value:{}", value);
    learningMetricsMeter.fetchCounter(TYPE_SOLVING).increment(value);
  }

}
