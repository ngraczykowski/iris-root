package com.silenteight.warehouse.report.statistics.simulation.calculation;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PercentageCalculator {

  @NonNull
  public Double calculate(long count, long total) {
    return count * 100 / (double) total;
  }
}
