package com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
class TimeSemaphore {

  private final AtomicLong activeUntil = new AtomicLong();

  void activate(long durationInSeconds) {
    activeUntil.set(now() + durationInSeconds);
  }

  boolean isActive() {
    return activeUntil.get() > now();
  }

  private static long now() {
    return Instant.now().getEpochSecond();
  }
}
