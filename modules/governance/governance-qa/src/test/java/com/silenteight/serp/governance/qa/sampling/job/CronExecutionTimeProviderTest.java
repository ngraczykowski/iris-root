package com.silenteight.serp.governance.qa.sampling.job;

import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;

class CronExecutionTimeProviderTest {

  @ParameterizedTest
  @MethodSource("provideExecutionTime")
  void executionTimeShouldReturnTimeOnFirstDayAtFirst(String checkedAt, String expected) {
    String onceInMonthAtFirstCronExpression = "0 0 1 1 * ?";

    CronExecutionTimeProvider cronExecutionTimeProvider = new CronExecutionTimeProvider(
        onceInMonthAtFirstCronExpression,
        new MockTimeSource(Instant.parse(checkedAt)));

    assertThat(cronExecutionTimeProvider.executionTime()).isEqualTo(parse(expected));
  }

  private static Stream<Arguments> provideExecutionTime() {
    return Stream.of(
        Arguments.of("2021-06-01T01:01:00Z", "2021-06-01T01:00:00Z"),
        Arguments.of("2021-06-15T12:01:30Z", "2021-06-01T01:00:00Z"),
        Arguments.of("2021-06-30T00:00:30Z", "2021-06-01T01:00:00Z")
    );
  }
}
