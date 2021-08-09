package com.silenteight.hsbc.bridge.agent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RequiredArgsConstructor
class AgentTimestampMapper {

  private final DateTimeFormatter dateTimeFormatter;

  long toUnixTimestamp(@NonNull String rawDate) {
    try {
      var date = LocalDate.parse(rawDate, dateTimeFormatter);
      return date.atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
    } catch (DateTimeParseException e) {
      throw new DateParsingException(e);
    }
  }
}
