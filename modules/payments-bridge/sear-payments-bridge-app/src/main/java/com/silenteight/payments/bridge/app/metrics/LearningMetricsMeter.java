package com.silenteight.payments.bridge.app.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@Slf4j
@RequiredArgsConstructor
class LearningMetricsMeter implements MeterBinder {

  private static final String METRIC_NAME = "solving_and_learning_alerts";
  private static final String TAG_TYPE = "type";

  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(MeterRegistry registry) {
    this.meterRegistry = registry;
  }

  Counter fetchCounter(String type) {
    return Counter.builder(METRIC_NAME).tag(TAG_TYPE, type).register(this.meterRegistry);
  }
}
