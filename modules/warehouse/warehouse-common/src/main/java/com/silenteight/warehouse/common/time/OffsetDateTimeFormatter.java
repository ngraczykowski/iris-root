package com.silenteight.warehouse.common.time;


import com.silenteight.sep.base.common.time.DateFormatter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class OffsetDateTimeFormatter implements DateFormatter {

  public static final OffsetDateTimeFormatter INSTANCE = new OffsetDateTimeFormatter();

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss.SSSSSSXXX");

  @Override
  public String format(TemporalAccessor value) {
    return FORMATTER.format(value);
  }
}
