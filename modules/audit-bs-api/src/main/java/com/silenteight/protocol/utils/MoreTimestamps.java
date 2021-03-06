package com.silenteight.protocol.utils;

import lombok.NonNull;

import com.google.protobuf.Timestamp;
import com.google.protobuf.TimestampOrBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public final class MoreTimestamps {

  @NotNull
  public static Timestamp toTimestamp(@NonNull Instant instant) {
    return Timestamp
        .newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  @NotNull
  public static Timestamp toTimestamp(@NonNull OffsetDateTime offsetDateTime) {
    return toTimestamp(offsetDateTime.toInstant());
  }

  @NotNull
  public static Instant toInstant(@NonNull TimestampOrBuilder timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(),timestamp.getNanos());
  }

  @NotNull
  public static OffsetDateTime toOffsetDateTime(@NonNull TimestampOrBuilder timestamp) {
    Instant instant = toInstant(timestamp);
    return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
  }

  @NotNull
  public static Timestamp toTimestampOrDefault(
      @Nullable Instant instant,
      @NonNull Timestamp defaultTimestamp) {

    if (instant == null)
      return defaultTimestamp;

    return toTimestamp(instant);
  }

  private MoreTimestamps() {
  }
}
