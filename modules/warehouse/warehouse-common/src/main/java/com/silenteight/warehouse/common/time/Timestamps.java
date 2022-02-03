package com.silenteight.warehouse.common.time;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import com.google.protobuf.Timestamp;
import com.google.protobuf.TimestampOrBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@UtilityClass
public class Timestamps {

  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  @NotNull
  public Timestamp toTimestamp(@NonNull OffsetDateTime offsetDateTime) {
    return toTimestamp(offsetDateTime.toInstant());
  }

  @NotNull
  public Timestamp toTimestamp(@NonNull Instant instant) {
    return Timestamp
        .newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  @NotNull
  public OffsetDateTime toOffsetDateTime(@NonNull TimestampOrBuilder timestamp) {
    Instant instant = toInstant(timestamp);
    return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
  }

  @NotNull
  public Instant toInstant(@NonNull TimestampOrBuilder timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  @NotNull
  public java.sql.Timestamp toSqlTimestamp(String date) {
    DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(date));
    return java.sql.Timestamp.valueOf(localDateTime);
  }

  @NotNull
  public java.sql.Timestamp toSqlTimestamp(@NonNull OffsetDateTime offsetDateTime) {
    return java.sql.Timestamp.valueOf(
        offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
  }

  @NotNull
  public Timestamp toTimestampOrDefault(
      @Nullable Instant instant,
      @NonNull Timestamp defaultTimestamp) {

    if (instant == null)
      return defaultTimestamp;

    return toTimestamp(instant);
  }

  @NonNull
  public String toStringFormatIsoLocalDate(@NonNull OffsetDateTime from) {
    return from
        .toLocalDateTime()
        .atZone(UTC)
        .format(ISO_LOCAL_DATE);
  }
}
