package com.silenteight.sep.base.common.time;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.TimeZone;

@RequiredArgsConstructor
public class TimeConverter {

  private final TimeSource timeSource;

  public OffsetDateTime toOffsetFromMilli(long epochSeconds) {
    return toOffset(Instant.ofEpochMilli(epochSeconds));
  }

  public OffsetDateTime toOffset(Instant instant) {
    TimeZone timeZone = timeSource.timeZone();

    return OffsetDateTime.ofInstant(instant, timeZone.toZoneId());
  }
}
