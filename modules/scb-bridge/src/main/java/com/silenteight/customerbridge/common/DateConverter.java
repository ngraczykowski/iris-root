package com.silenteight.customerbridge.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class DateConverter {

  private static final long EPOCH_SECONDS_THRESHOLD = 10000000000L;
  private final DateTimeFormatter dateTimeFormatter;
  private final List<Function<String, TemporalAccessor>> parsers;

  public DateConverter(String timezone) {
    this.dateTimeFormatter =
        DateTimeFormatter.ofPattern("[yyyy/MM/dd HH:mm:ss[.SSS][.SS][.S]]"
                + "[yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]]")
            .withZone(ZoneId.of(timezone));

    this.parsers = Arrays.asList(
        this::useDateTimeFormat,
        DateConverter::useIsoDateTimeFormat,
        this::useDateTimeFormatTrimmedToMillis,
        this::useDateTimeFormatTrimmedToSeconds,
        DateConverter::useEpochConverter
    );
  }

  private TemporalAccessor useDateTimeFormat(String input) {
    return dateTimeFormatter.parse(input.trim());
  }

  private TemporalAccessor useDateTimeFormatTrimmedToSeconds(String input) {
    return dateTimeFormatter.parse(input.trim().substring(0, 19));
  }

  private TemporalAccessor useDateTimeFormatTrimmedToMillis(String input) {
    return dateTimeFormatter.parse(input.trim().substring(0, 23));
  }

  private static TemporalAccessor useIsoDateTimeFormat(String input) {
    return DateTimeFormatter.ISO_DATE_TIME.parse(input.trim());
  }

  private static TemporalAccessor useEpochConverter(String input) {
    long output = Long.parseLong(input.trim());

    if (output < EPOCH_SECONDS_THRESHOLD)
      return Instant.ofEpochSecond(output);
    else
      return Instant.ofEpochMilli(output);
  }

  @Nonnull
  public Optional<Instant> convert(@Nullable String input) {
    if (input == null)
      return empty();

    TemporalAccessor output = null;

    for (Function<String, TemporalAccessor> parser : parsers) {
      output = tryToParse(parser, input);

      if (output != null)
        break;
    }

    return ofNullable(output).map(Instant::from);
  }

  private static TemporalAccessor tryToParse(
      Function<String, TemporalAccessor> parser, String input) {

    try {
      return parser.apply(input);
    } catch (RuntimeException e) {
      // exception is swallowed quietly by design
      return null;
    }
  }
}
