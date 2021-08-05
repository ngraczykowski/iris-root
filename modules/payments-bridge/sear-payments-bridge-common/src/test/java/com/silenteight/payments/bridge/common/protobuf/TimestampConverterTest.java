package com.silenteight.payments.bridge.common.protobuf;

import com.google.protobuf.util.Timestamps;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.*;
import static org.assertj.core.api.Assertions.*;

class TimestampConverterTest {

  @Test
  void timestampFromInstant() {
    var timestamp = fromInstant(Instant.parse("2007-12-03T10:15:30.00Z"));
    assertThat(Timestamps.toString(timestamp)).isEqualTo("2007-12-03T10:15:30Z");
  }

  @Test
  void timestampFromSqlTimestamp() {
    var timestamp = fromSqlTimestamp(java.sql.Timestamp.valueOf("1983-05-24 12:34:56.0000"));
    assertThat(Timestamps.toString(timestamp)).isEqualTo("1983-05-24T12:34:56Z");
  }

  @Test
  void timestampFromOffsetDateTime() {
    var timestamp = fromOffsetDateTime(OffsetDateTime.parse("1983-05-24T12:34:56.78+02:00"));
    assertThat(Timestamps.toString(timestamp)).isEqualTo("1983-05-24T10:34:56.780Z");
  }

  @Test
  void timestampToOffsetDateTime() throws ParseException {
    var offsetDateTime = toOffsetDateTime(Timestamps.parse("1983-05-24T10:34:56.780Z"));
    assertThat(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime))
        .isEqualTo("1983-05-24T10:34:56.78Z");
  }
}
