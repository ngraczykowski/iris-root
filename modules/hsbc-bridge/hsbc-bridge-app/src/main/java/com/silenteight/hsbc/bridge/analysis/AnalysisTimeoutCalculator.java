package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
class AnalysisTimeoutCalculator {

  private final Duration timeout;

  OffsetDateTime determineTimeout(long alertsCount) {
    var timeout = calculateTimeout(alertsCount);
    return OffsetDateTime.now().plus(timeout);
  }

  private Duration calculateTimeout(long alertsCount) {
    var multiplier = (alertsCount / 5000) + 1;
    return timeout.multipliedBy(multiplier);
  }
}
