package com.silenteight.sens.webapp.common.testing.time;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sens.webapp.common.time.TimeSource;

import java.time.Instant;
import java.util.TimeZone;

import static com.silenteight.sens.webapp.common.time.ApplicationTimeZone.TIME_ZONE;
import static java.time.Instant.ofEpochMilli;

@RequiredArgsConstructor
public class MockTimeSource implements TimeSource {

  public static final MockTimeSource ARBITRARY_INSTANCE =
      new MockTimeSource(ofEpochMilli(1580728524L));

  @NonNull
  private final Instant instant;

  @Setter
  @NonNull
  private TimeZone timeZone = TIME_ZONE;

  @Override
  public Instant now() {
    return instant;
  }

  @Override
  public TimeZone timeZone() {
    return timeZone;
  }
}
