package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.serp.governance.qa.sampling.generator.exception.InvalidTotalAlertCountException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

class DistributionCalculatorTest {

  DistributionCalculator underTest;
  private final Long totalSampleCount = 454L;
  private final Long totalOverallCount = 102800L;

  @ParameterizedTest
  @MethodSource("provideAlertsCount")
  void calculateAmountShouldReturnCorrectValue(Long countRiskType, Long expectedSampleCount) {
    underTest = new DistributionCalculator(totalSampleCount, totalOverallCount);
    assertThat(underTest.calculateAmount(countRiskType)).isEqualTo(expectedSampleCount);
  }

  private static Stream<Arguments> provideAlertsCount() {
    return Stream.of(
        Arguments.of(1L, 1L),
        Arguments.of(50000L, 221L),
        Arguments.of(27000L, 120L),
        Arguments.of(18000L, 80L),
        Arguments.of(7000L, 31L),
        Arguments.of(800L, 4L)
    );
  }

  @Test
  void calculateAmountShouldReturnZero_whenZeroCountRiskType() {
    underTest = new DistributionCalculator(totalSampleCount, totalOverallCount);
    assertThat(underTest.calculateAmount(0L)).isEqualTo(0L);
  }

  @Test
  void calculateAmountShouldReturnZero_whenZeroCTotalSampleCount() {
    underTest = new DistributionCalculator(0L, totalOverallCount);
    assertThat(underTest.calculateAmount(454L)).isEqualTo(0L);
  }

  @Test
  void calculateAmountShouldThrowException_whenZeroTotalOverallCount() {
    assertThatThrownBy(() -> new DistributionCalculator(totalSampleCount, 0L))
        .isInstanceOf(InvalidTotalAlertCountException.class)
        .hasMessageContaining(format("Invalid total alert count=%d", 0L));
  }
}
