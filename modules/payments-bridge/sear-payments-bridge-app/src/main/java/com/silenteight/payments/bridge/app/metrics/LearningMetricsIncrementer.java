package com.silenteight.payments.bridge.app.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.metrics.LearningMetricsIncrementerPort;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LearningMetricsIncrementer implements LearningMetricsIncrementerPort {

  private final LearningMetricsMeter learningMetricsMeter;
  private static final String TYPE_LEARNING = "learning";

  @Override
  public void increment(double value) {
    log.debug("Increment metrics counter learning value:{}", value);
    learningMetricsMeter.fetchCounter(TYPE_LEARNING).increment(value);
  }
}
