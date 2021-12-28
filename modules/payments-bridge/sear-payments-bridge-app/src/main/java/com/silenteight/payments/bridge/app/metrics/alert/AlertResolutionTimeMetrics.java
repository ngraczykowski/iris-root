package com.silenteight.payments.bridge.app.metrics.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.metrics.alert.AlertResolutionEndEvent;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
class AlertResolutionTimeMetrics {

  public static final String PB_ALERT_RESOLUTION_TIME = "pb.alert.resolution.time";
  public static final String TAG_TIME_KEY = "time";
  public static final String TAG_TIME_VALUE = "alert.resolution.time";

  private final MeterRegistry meterRegistry;
  private final AlertMessageUseCase alertMessageUseCase;

  @EventListener
  public void onAlertResolution(AlertResolutionEndEvent event) {
    recordDuration(Duration.ofMillis(
        event.getEndTime() - alertMessageUseCase.findReceivedAtTimeMilli(event.getAlertId())));
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

}
