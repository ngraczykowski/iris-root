package com.silenteight.sens.webapp.common.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTimeSourceTest {

  @Mock
  private Clock clock;

  private TimeZone timeZone = TimeZone.getDefault();

  private Instant instant = Instant.ofEpochSecond(123456789, 123456);

  @Test
  void testDefaultTimeSource() {
    OffsetDateTime expectedOffsetDateTime = OffsetDateTime.ofInstant(instant, timeZone.toZoneId());
    LocalDateTime expectedLocalDateTime = LocalDateTime.ofInstant(instant, timeZone.toZoneId());

    when(clock.instant()).thenReturn(instant);

    DefaultTimeSource source = new DefaultTimeSource(clock, timeZone);

    assertThat(source.now()).isEqualTo(instant);
    assertThat(source.timeZone()).isEqualTo(timeZone);
    assertThat(source.offsetDateTime()).isEqualTo(expectedOffsetDateTime);
    assertThat(source.localDateTime()).isEqualTo(expectedLocalDateTime);
  }
}
