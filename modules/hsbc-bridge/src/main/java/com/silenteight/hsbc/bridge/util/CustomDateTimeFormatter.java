package com.silenteight.hsbc.bridge.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

@Component
public class CustomDateTimeFormatter {

  private final String pattern;

  public CustomDateTimeFormatter(
      @Value("${silenteight.bridge.alert.datetime.format}") String pattern) {
    this.pattern = pattern;
  }

  public DateTimeFormatter getDateTimeFormatter() {
    return new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern(pattern)
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .toFormatter(Locale.ENGLISH);
  }
}
