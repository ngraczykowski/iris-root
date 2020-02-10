package com.silenteight.sens.webapp.common.time;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.TimeZone;

@RequiredArgsConstructor
public class TimeConverter {

  private final TimeSource timeSource;

  public OffsetDateTime toOffsetFromSeconds(long epochSeconds) {
    return toOffset(Instant.ofEpochSecond(epochSeconds));
  }

  public OffsetDateTime toOffset(Instant instant) {
    TimeZone timeZone = timeSource.timeZone();

    return OffsetDateTime.ofInstant(instant, timeZone.toZoneId());
  }
}
