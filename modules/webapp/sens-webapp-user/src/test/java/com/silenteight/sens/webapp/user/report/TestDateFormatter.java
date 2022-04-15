package com.silenteight.sens.webapp.user.report;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DateFormatter;

import java.time.temporal.TemporalAccessor;

@RequiredArgsConstructor
public class TestDateFormatter implements DateFormatter {

  private final String fixedValue;

  @Override
  public String format(TemporalAccessor value) {
    return format(value.toString());
  }

  String format(String value) {
    return String.join("_", fixedValue, value);
  }
}
