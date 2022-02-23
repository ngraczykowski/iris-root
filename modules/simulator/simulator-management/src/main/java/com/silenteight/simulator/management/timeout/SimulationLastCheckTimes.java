package com.silenteight.simulator.management.timeout;

import com.silenteight.sep.base.common.time.TimeSource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.time.Instant;

import static java.lang.Math.max;
import static java.time.Duration.ofMinutes;

class SimulationLastCheckTimes {

  private final TimeSource timeSource;

  private final Duration interval;

  private final LoadingCache<String, Instant> simulationCheckCache;

  SimulationLastCheckTimes(TimeSource timeSource, Duration interval) {
    this.timeSource = timeSource;
    this.interval = interval;

    simulationCheckCache = crateCache(timeSource, interval);
  }

  private static Duration calculateExpireDuration(Duration interval) {
    return ofMinutes(max(1, interval.toMinutes()) * 2);
  }

  private static LoadingCache<String, Instant> crateCache(
      TimeSource timeSource, Duration interval) {
    return CacheBuilder.newBuilder()
        .expireAfterAccess(calculateExpireDuration(interval))
        .build(new CacheLoader<>() {
          @Override
          public Instant load(String key) {
            return timeSource.now();
          }
        });
  }

  boolean isIntervalElapsed(String cacheKey) {
    Instant lastCheck = simulationCheckCache.getUnchecked(cacheKey);
    Instant now = timeSource.now();
    return lastCheck.plus(interval).isBefore(now);
  }

  void updateSimulationCheckTimestamp(String cacheKey) {
    Instant now = timeSource.now();
    simulationCheckCache.put(cacheKey, now);
  }
}
