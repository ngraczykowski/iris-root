package com.silenteight.searpaymentsmockup;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class NamedThreadFactory implements ThreadFactory {
  private final AtomicInteger sequence = new AtomicInteger(1);
  private final String prefix;

  @Override
  public Thread newThread(@NonNull Runnable r) {
    Thread thread = new Thread(r);
    int seq = sequence.getAndIncrement();
    thread.setName(prefix + "-" + seq);
    if (!thread.isDaemon())
      thread.setDaemon(true);
    return thread;
  }
}
