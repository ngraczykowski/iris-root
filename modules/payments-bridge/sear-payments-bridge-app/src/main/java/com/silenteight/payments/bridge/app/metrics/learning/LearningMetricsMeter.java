package com.silenteight.payments.bridge.app.metrics.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.learning.event.AlreadySolvedAlertEvent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class LearningMetricsMeter implements MeterBinder {

  private static final String TYPE_SOLVING = "solving";
  private static final String TYPE_LEARNING_ALREADY_SOLVED = "learning.already.solved";
  private static final String SOLVING_LEARNING_COUNTER = "pb.solving.and.learning.alerts";

  private static final String TAG_TYPE = "type";

  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(MeterRegistry registry) {
    this.meterRegistry = registry;
    fetchCounter(TYPE_SOLVING);
    fetchCounter(TYPE_LEARNING_ALREADY_SOLVED);
  }

  Counter fetchCounter(String type) {
    return Counter
        .builder(SOLVING_LEARNING_COUNTER)
        .tag(TAG_TYPE, type)
        .register(this.meterRegistry);
  }

  @EventListener
  void onAlreadySolvedAlertEvent(AlreadySolvedAlertEvent event) {
    log.debug("Increment metrics counter on already registered alerts by: {}", event.getCounter());
    fetchCounter(TYPE_LEARNING_ALREADY_SOLVED).increment(event.getCounter());
  }

  @EventListener
  void onSolvingEvent(AlertRegisteredEvent event) {
    log.debug(
        "Increment metrics counter solving value for alertId: {}",
        event.getAeAlert().getAlertId());
    fetchCounter(TYPE_SOLVING).increment();
  }
}
