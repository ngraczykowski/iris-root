package com.silenteight.serp.common.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
class ProcessProgressLogger {

  private final AtomicLong counter = new AtomicLong();

  @NonNull
  private final Logger logger;
  @NonNull
  private final String processName;
  private final int step;

  void tick() {
    long count = counter.incrementAndGet();
    if (count % step == 0)
      logger.info("Processed [{}] {} messages.", processName, count);
  }
}
