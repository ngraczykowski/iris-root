package com.silenteight.sens.webapp.common.testing.time;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sens.webapp.common.time.TimeSource;

import java.time.Instant;
import java.util.TimeZone;

@RequiredArgsConstructor
public class MockTimeSource implements TimeSource {

  @NonNull
  private final Instant instant;

  @Setter
  @NonNull
  private TimeZone timeZone = TimeZone.getDefault();

  @Override
  public Instant now() {
    return instant;
  }

  @Override
  public TimeZone timeZone() {
    return timeZone;
  }
}
