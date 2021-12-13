package com.silenteight.payments.bridge.app.metrics.efficiency;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.metrics.efficiency.EfficiencyMetricIncrementerPort;

import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EfficiencyMetricsIncrementer implements EfficiencyMetricIncrementerPort {

  private final EfficiencyMetricsMeter efficiencyMetricsMeter;

  @Override
  public void incrementManualInvestigation() {
    incrementCounter("MANUAL_INVESTIGATION");
  }

  @Override
  public void incrementRecommendedAction(String recommendedAction) {
    incrementCounter(recommendedAction);
  }

  private void incrementCounter(String counterTagName) {
    Counter counter = this.efficiencyMetricsMeter.fetchCounter(counterTagName);
    counter.increment();
  }

}
