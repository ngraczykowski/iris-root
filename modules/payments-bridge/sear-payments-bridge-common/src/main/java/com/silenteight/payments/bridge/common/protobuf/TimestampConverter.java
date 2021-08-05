package com.silenteight.payments.bridge.common.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.annotation.Nonnull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimestampConverter {

  private static final int MILLIS_PER_SECOND = 1000;

  @Nonnull
  public static Timestamp fromInstant(@NonNull Instant instant) {
    return Timestamp
        .newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  @Nonnull
  public static Instant toInstant(@NonNull Timestamp timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  @Nonnull
  public static Timestamp fromSqlTimestamp(@Nonnull java.sql.Timestamp timestamp) {
    return Timestamp.newBuilder()
        .setSeconds(timestamp.getTime() / MILLIS_PER_SECOND)
        .setNanos(timestamp.getNanos())
        .build();
  }

  @Nonnull
  public static Timestamp fromOffsetDateTime(@NonNull OffsetDateTime offsetDateTime) {
    return Timestamp.newBuilder()
        .setSeconds(offsetDateTime.toEpochSecond())
        .setNanos(offsetDateTime.getNano())
        .build();
  }

  @Nonnull
  public static OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
    return OffsetDateTime.ofInstant(toInstant(timestamp), ZoneOffset.UTC);
  }
}
