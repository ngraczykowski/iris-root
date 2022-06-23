package com.silenteight.serp.governance.qa.sampling.job;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

class DateRangeDtoProviderTest {

  @Test
  void latestDateRangeAtHalfMonthShouldReturnEntireMonthDateRange() {
    String onceInMonthAtFirstCronExpression = "0 0 1 1 * ?";
    DateRangeProvider dateRangeProvider = new DateRangeProvider(
        onceInMonthAtFirstCronExpression,
        new MockTimeSource(Instant.parse("2021-06-16T12:01:30Z")));

    DateRangeDto dateRangeDto = dateRangeProvider.latestDateRange();
    assertThat(dateRangeDto.getFrom()).isEqualTo(OffsetDateTime.parse("2021-05-01T01:00:00Z"));
    assertThat(dateRangeDto.getTo()).isEqualTo(OffsetDateTime.parse("2021-06-01T01:00:00Z"));
  }

  @Test
  void latestDateRangeAtMonthsBeginningShouldReturnEntireMonthDateRange() {
    String onceInMonthAtFirstCronExpression = "0 0 1 1 * ?";
    DateRangeProvider dateRangeProvider = new DateRangeProvider(
        onceInMonthAtFirstCronExpression,
        new MockTimeSource(Instant.parse("2021-06-01T12:01:30Z")));

    DateRangeDto dateRangeDto = dateRangeProvider.latestDateRange();
    assertThat(dateRangeDto.getFrom()).isEqualTo(OffsetDateTime.parse("2021-05-01T01:00:00Z"));
    assertThat(dateRangeDto.getTo()).isEqualTo(OffsetDateTime.parse("2021-06-01T01:00:00Z"));
  }

  @Test
  void latestDateRangeAtMonthsEndShouldReturnEntireMonthDateRange() {
    String onceInMonthAtFirstCronExpression = "0 0 0 1 * ?";
    DateRangeProvider dateRangeProvider = new DateRangeProvider(
        onceInMonthAtFirstCronExpression,
        new MockTimeSource(Instant.parse("2021-06-30T23:59:59Z")));

    DateRangeDto dateRangeDto = dateRangeProvider.latestDateRange();
    assertThat(dateRangeDto.getFrom()).isEqualTo(OffsetDateTime.parse("2021-05-01T00:00:00Z"));
    assertThat(dateRangeDto.getTo()).isEqualTo(OffsetDateTime.parse("2021-06-01T00:00:00Z"));
  }
}
