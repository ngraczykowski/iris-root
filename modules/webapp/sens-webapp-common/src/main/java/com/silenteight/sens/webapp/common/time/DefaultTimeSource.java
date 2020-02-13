package com.silenteight.sens.webapp.common.time;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.TimeZone;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultTimeSource implements TimeSource {

  public static final DefaultTimeSource INSTANCE =
      new DefaultTimeSource(Clock.systemUTC(), TimeZone.getDefault());

  public static final TimeConverter TIME_CONVERTER = new TimeConverter(INSTANCE);

  private final Clock clock;
  private final TimeZone timeZone;

  @Override
  public Instant now() {
    return clock.instant();
  }

  @Override
  public TimeZone timeZone() {
    return timeZone;
  }
}

