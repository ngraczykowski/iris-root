package com.silenteight.payments.bridge.app.metrics.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.metrics.alert.AlertResolutionEndEvent;
import com.silenteight.payments.bridge.firco.metrics.alert.AlertResolutionStartEvent;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
@Slf4j
class AlertResolutionTimeMetrics implements MeterBinder {

  public static final String PB_ALERT_RESOLUTION_TIME = "pb.alert.resolution.time";
  public static final String TAG_TIME_KEY = "time";
  public static final String TAG_TIME_VALUE = "alert.resolution.time";
  private MeterRegistry meterRegistry;

  private final Map<UUID, Long> alertCache = new ConcurrentHashMap<>();

  @EventListener
  public void onAlertResolution(AlertResolutionStartEvent event) {
    alertCache.put(event.getAlertId(), event.getStartTime());
  }

  @EventListener
  public void onAlertResolution(AlertResolutionEndEvent event) {
    Optional
        .ofNullable(alertCache.remove(event.getAlertId()))
        .ifPresentOrElse(
            startTime -> recordDuration(Duration.ofMillis(event.getEndTime() - startTime)),
            () -> log.debug("Alert created time not found, for alertId = {}", event.getAlertId())
        );
  }

  private void recordDuration(Duration duration) {
    this.fetchTimer().record(duration);
  }

  private Timer fetchTimer() {
    return Timer
        .builder(PB_ALERT_RESOLUTION_TIME)
        .tag(TAG_TIME_KEY, TAG_TIME_VALUE)
        .register(this.meterRegistry);
  }

  @Override
  public void bindTo(@Nonnull MeterRegistry registry) {
    this.meterRegistry = registry;
  }
}
