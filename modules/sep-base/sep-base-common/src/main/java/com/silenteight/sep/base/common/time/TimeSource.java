package com.silenteight.sep.base.common.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

public interface TimeSource {

  /**
   * Returns {@link Instant} of now.
   *
   * @return point-in-time of now
   */
  Instant now();

  /**
   * Returns currently set default time zone.
   *
   * @return default time zone
   */
  TimeZone timeZone();

  default OffsetDateTime offsetDateTime() {
    return OffsetDateTime.ofInstant(now(), timeZone().toZoneId());
  }

  default LocalDateTime localDateTime() {
    return LocalDateTime.ofInstant(now(), timeZone().toZoneId());
  }
}
