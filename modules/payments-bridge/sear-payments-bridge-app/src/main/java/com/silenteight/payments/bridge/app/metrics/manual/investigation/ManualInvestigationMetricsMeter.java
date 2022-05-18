package com.silenteight.payments.bridge.app.metrics.manual.investigation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.events.ManualInvestigationReasonEvent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class ManualInvestigationMetricsMeter implements MeterBinder {

  private static final String MANUAL_INVESTIGATION_REASON = "pb.manual.investigation.reason";
  private static final String TAG_REASON = "reason";

  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(MeterRegistry registry) {
    this.meterRegistry = registry;
  }

  private Counter fetchCounter(String reason) {
    return Counter
        .builder(MANUAL_INVESTIGATION_REASON)
        .tag(TAG_REASON, reason)
        .register(this.meterRegistry);
  }

  @EventListener
  void onManualInvestigationReason(ManualInvestigationReasonEvent event) {
    log.debug("Manual Investigation reason event received: {}", event.getReason());
    fetchCounter(event.getReason()).increment();
  }
}
