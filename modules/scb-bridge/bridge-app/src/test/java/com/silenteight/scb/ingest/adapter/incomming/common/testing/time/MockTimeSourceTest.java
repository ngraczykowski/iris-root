package com.silenteight.scb.ingest.adapter.incomming.common.testing.time;

import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

class MockTimeSourceTest {

  @Test
  void testMockTimeSource() {
    Instant expectedInstant = Instant.now();
    TimeZone expectedTimeZone = TimeZone.getDefault();
    OffsetDateTime expectedOffsetDateTime =
        OffsetDateTime.ofInstant(expectedInstant, expectedTimeZone.toZoneId());
    LocalDateTime expectedLocalDateTime =
        LocalDateTime.ofInstant(expectedInstant, expectedTimeZone.toZoneId());

    MockTimeSource source = new MockTimeSource(expectedInstant);
    source.setTimeZone(expectedTimeZone);

    assertThat(source.now()).isEqualTo(expectedInstant);
    assertThat(source.timeZone()).isEqualTo(expectedTimeZone);
    assertThat(source.offsetDateTime()).isEqualTo(expectedOffsetDateTime);
    assertThat(source.localDateTime()).isEqualTo(expectedLocalDateTime);
  }

  @Test
  void cannotCreateWithNulls() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> new MockTimeSource(null));

    MockTimeSource source = new MockTimeSource(Instant.now());

    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> source.setTimeZone(null));
  }
}
