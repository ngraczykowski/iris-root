package com.silenteight.sep.base.testing.time;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sep.base.common.time.TimeSource;

import java.time.Instant;
import java.util.TimeZone;

import static java.time.Instant.ofEpochMilli;

@RequiredArgsConstructor
public class MockTimeSource implements TimeSource {

  public static final MockTimeSource ARBITRARY_INSTANCE =
      new MockTimeSource(ofEpochMilli(1580728524L));

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
