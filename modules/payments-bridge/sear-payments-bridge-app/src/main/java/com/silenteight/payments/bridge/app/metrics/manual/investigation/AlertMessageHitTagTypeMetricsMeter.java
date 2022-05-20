package com.silenteight.payments.bridge.app.metrics.manual.investigation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlertMessageHitTagTypeMetricsMeter implements MeterBinder {

  private static final String ALERT_MESSAGE_HIT_TAG = "pb.alert.message.hit.tag.type";
  private static final String HIT_TAG = "hit.tag";

  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(MeterRegistry registry) {
    this.meterRegistry = registry;
  }

  @EventListener
  void onAlertMessage(FircoAlertMessage message) {
    log.debug("Alert message received to count hit tag types, messageId: {}", message.getId());
    var tagCounters = message.countHitTagsByType();
    tagCounters.keySet().forEach(key -> {
      var count = tagCounters.get(key);
      log.debug("Tag type: {} metrics counter increased by: {}", key, count);
      fetchCounter(key).increment(count);
    });
  }


  private Counter fetchCounter(String hitTag) {
    return Counter
        .builder(ALERT_MESSAGE_HIT_TAG)
        .tag(HIT_TAG, hitTag)
        .register(this.meterRegistry);
  }
}
