package com.silenteight.warehouse.common.calculation;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

class PercentageCalculatorTest {

  @Test
  void shouldReturnNaNWhenTotalZero() {
    assertThat(PercentageCalculator.calculate(0L, 0L)).isNaN();
  }

  @Test
  void shouldReturnZeroWhenCountZero() {
    assertThat(PercentageCalculator.calculate(0L, 10L)).isZero();
  }

  @ParameterizedTest
  @MethodSource("provideDataForPercentageCalculator")
  void shouldCalculateResults(long count, long total, double expectedValue) {
    assertThat(PercentageCalculator.calculate(count, total)).isEqualTo(expectedValue);
  }

  private static Stream<Arguments> provideDataForPercentageCalculator() {
    return Stream.of(
        of(100, 100, 100.0),
        of(1, 2L, 50.0),
        of(15, 10, 150.0),
        of(2, 3, 66.66666666666667)
    );
  }

}
