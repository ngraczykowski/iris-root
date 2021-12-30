package com.silenteight.payments.bridge.app.metrics.efficiency;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.metrics.efficiency.EfficiencyMetricIncrementerPort;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EfficiencyMetricsIncrementer implements EfficiencyMetricIncrementerPort {

  private static final String PB_EFFICIENCY_METRICS_PREFIX = "pb.metrics.efficiency";
  private static final String TAG_TYPE = "action";
  private final MeterRegistry meterRegistry;

  @Override
  public void incrementManualInvestigation() {
    incrementCounter("MANUAL_INVESTIGATION");
  }

  @Override
  public void incrementRecommendedAction(String recommendedAction) {
    incrementCounter(recommendedAction);
  }

  private void incrementCounter(String counterTagName) {
    Counter counter = this.fetchCounter(counterTagName);
    counter.increment();
  }

  Counter fetchCounter(String action) {
    return Counter
        .builder(PB_EFFICIENCY_METRICS_PREFIX)
        .tag(TAG_TYPE, action)
        .register(this.meterRegistry);
  }

}
