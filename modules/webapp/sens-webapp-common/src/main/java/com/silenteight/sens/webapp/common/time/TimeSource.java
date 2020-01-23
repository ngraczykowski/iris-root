package com.silenteight.sens.webapp.common.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

public interface TimeSource {

  default OffsetDateTime offsetDateTime() {
    return OffsetDateTime.ofInstant(now(), timeZone().toZoneId());
  }

  Instant now();

  TimeZone timeZone();

  default LocalDateTime localDateTime() {
    return LocalDateTime.ofInstant(now(), timeZone().toZoneId());
  }
}
