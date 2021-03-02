package com.silenteight.adjudication.app;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.events.ClearSensitiveDataEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Component
@Slf4j
public class ClearSensitiveDataEventPublisher {

  private ApplicationEventPublisher applicationEventPublisher;

  @Scheduled(cron = "${clear-sensitive-data.cron.expression}")
  public void publishClearSensitiveDataEvent() {
    OffsetDateTime dataThreshold = OffsetDateTime.now().minus(Duration.ofDays(1));
    log.info("Publishing clear sensitive data event");
    ClearSensitiveDataEvent clearSensitiveDataEvent =
        new ClearSensitiveDataEvent(this, dataThreshold);
    applicationEventPublisher.publishEvent(clearSensitiveDataEvent);
  }
}
