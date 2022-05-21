package com.silenteight.hsbc.datasource.util;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;

@UtilityClass
public class IsPepTimestampUtil {

  public static Long toUnixTimestamp(String rawDate) {
    if (StringUtils.isBlank(rawDate)) {
      return 0L;
    }

    try {
      var dateTime = LocalDateTime.parse(rawDate, getDateTimeFormatter());
      return dateTime.atOffset(ZoneOffset.UTC).toEpochSecond();
    } catch (DateTimeParseException e) {
      throw new DateTimeParsingException(e);
    }
  }

  private static DateTimeFormatter getDateTimeFormatter() {
    return new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("[dd-MMM-yyyy]")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .toFormatter(Locale.ENGLISH);
  }

  private static class DateTimeParsingException extends RuntimeException {

    private static final long serialVersionUID = 640726327424095224L;

    public DateTimeParsingException(Throwable cause) {
      super(cause);
    }
  }
}
