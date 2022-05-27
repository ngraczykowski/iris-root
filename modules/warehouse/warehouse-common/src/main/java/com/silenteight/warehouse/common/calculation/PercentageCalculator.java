package com.silenteight.warehouse.common.calculation;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PercentageCalculator {

  @NonNull
  public Double calculate(double count, double total) {
    return count * 100 / total;
  }
}
