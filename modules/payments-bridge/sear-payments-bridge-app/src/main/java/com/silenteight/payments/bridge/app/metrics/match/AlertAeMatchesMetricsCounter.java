package com.silenteight.payments.bridge.app.metrics.match;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AlertAeMatchesMetricsCounter {

  private static final String COUNTER_NAME = "pb.alert.ae.match";
  private static final String TAG_TYPE = "matches";
  private static final String TOTAL_MATCHES = "Total matches";

  private final MeterRegistry meterRegistry;

  @EventListener
  public void incrementManualInvestigation(RegisterAlertResponse registerAlertResponse) {
    incrementCounter(registerAlertResponse.getMatchResponses().size());
  }

  private void incrementCounter(int amount) {
    this.fetchCounter().increment(amount);
  }

  private Counter fetchCounter() {
    return Counter
        .builder(COUNTER_NAME)
        .tag(TAG_TYPE, TOTAL_MATCHES)
        .register(this.meterRegistry);
  }

}
