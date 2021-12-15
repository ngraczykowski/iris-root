package com.silenteight.payments.bridge.app.metrics.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.metrics.LearningForSolvingMetricsIncrementerPort;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.app.metrics.learning.LearningMetricsMeter.TYPE_LEARNING;

@Service
@Slf4j
@RequiredArgsConstructor
public class LearningForSolvingMetricsIncrementer implements
    LearningForSolvingMetricsIncrementerPort {

  private final LearningMetricsMeter learningMetricsMeter;

  @Override
  public void increment(double value) {
    log.debug("Increment metrics counter learning value:{}", value);
    learningMetricsMeter.fetchCounter(TYPE_LEARNING).increment(value);
  }
}
