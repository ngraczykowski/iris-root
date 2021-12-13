package com.silenteight.payments.bridge.app.metrics.efficiency;

import lombok.RequiredArgsConstructor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
class EfficiencyMetricsMeter implements MeterBinder {

  private static final String PB_EFFICIENCY_METRICS_PREFIX = "pb.metrics.efficiency";
  private static final String TAG_TYPE = "action";
  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(@Nonnull MeterRegistry registry) {
    this.meterRegistry = registry;
  }

  Counter fetchCounter(String action) {
    return Counter
        .builder(PB_EFFICIENCY_METRICS_PREFIX)
        .tag(TAG_TYPE, action)
        .register(this.meterRegistry);
  }
}
