package com.silenteight.warehouse.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AggregationPeriod {
  DAILY(1),
  WEEKLY(7);

  private final Integer numberOfDays;
}
