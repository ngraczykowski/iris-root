package com.silenteight.recommendation.api.library.v1;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.google.protobuf.Timestamp;
import com.google.protobuf.TimestampOrBuilder;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimestampUtil {

  public static OffsetDateTime toOffsetDateTime(@NonNull TimestampOrBuilder timestamp) {
    Instant instant = toInstant(timestamp);
    return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
  }

  public static Instant toInstant(@NonNull TimestampOrBuilder timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  public static Timestamp fromOffsetDateTime(@NonNull OffsetDateTime offsetDateTime) {
    return Timestamp.newBuilder()
        .setSeconds(offsetDateTime.toEpochSecond())
        .setNanos(offsetDateTime.getNano())
        .build();
  }
}
