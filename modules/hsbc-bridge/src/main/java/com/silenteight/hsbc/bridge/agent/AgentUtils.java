package com.silenteight.hsbc.bridge.agent;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@UtilityClass
class AgentUtils {

  private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
      .parseCaseInsensitive().appendPattern("dd-MMM-yy")
      .toFormatter(Locale.ENGLISH);

  static long toUnixTimestamp(@NonNull String rawDate) {
    try {
      var date = LocalDate.parse(rawDate, DATE_FORMATTER);
      return date.atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
    } catch (DateTimeParseException e) {
      throw new DateParsingException(e);
    }
  }
}
