package com.silenteight.payments.bridge.app.metrics.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@Slf4j
@RequiredArgsConstructor
class LearningMetricsMeter implements MeterBinder {

  static final String TYPE_SOLVING = "solving";
  static final String TYPE_LEARNING = "learning-for-solving";
  private static final String SOLVING_LEARNING_COUNTER = "solving_and_learning_alerts";

  private static final String TAG_TYPE = "type";

  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(MeterRegistry registry) {
    this.meterRegistry = registry;
    fetchCounter(TYPE_SOLVING);
    fetchCounter(TYPE_LEARNING);
  }

  Counter fetchCounter(String type) {
    return Counter
        .builder(SOLVING_LEARNING_COUNTER)
        .tag(TAG_TYPE, type)
        .register(this.meterRegistry);
  }
}
