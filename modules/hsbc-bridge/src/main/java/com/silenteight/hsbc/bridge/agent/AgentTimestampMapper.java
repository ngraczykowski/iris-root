package com.silenteight.hsbc.bridge.agent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RequiredArgsConstructor
class AgentTimestampMapper {

  private final DateTimeFormatter dateTimeFormatter;

  long toUnixTimestamp(@NonNull String rawDate) {
    try {
      var dateTime = LocalDateTime.parse(rawDate, dateTimeFormatter);
      return dateTime.atOffset(ZoneOffset.UTC).toEpochSecond();
    } catch (DateTimeParseException e) {
      throw new DateTimeParsingException(e);
    }
  }
}
