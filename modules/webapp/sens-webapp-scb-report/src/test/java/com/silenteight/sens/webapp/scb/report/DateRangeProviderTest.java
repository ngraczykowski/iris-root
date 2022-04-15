package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

class DateRangeProviderTest {

  @Test
  void returnsDateRangeOfLast12Hours() {
    String midnightAndNoonCronExpression = "0 0 0/12 ? * *";
    DateRangeProvider dateRangeProvider = new DateRangeProvider(
        midnightAndNoonCronExpression,
        new MockTimeSource(Instant.parse("2020-04-22T12:01:30Z")));

    DateRange dateRange = dateRangeProvider.latestDateRange();
    assertThat(dateRange.getFrom()).isEqualTo(OffsetDateTime.parse("2020-04-22T00:00:00Z"));
    assertThat(dateRange.getTo()).isEqualTo(OffsetDateTime.parse("2020-04-22T12:00:00Z"));

    dateRangeProvider = new DateRangeProvider(
        midnightAndNoonCronExpression,
        new MockTimeSource(Instant.parse("2020-04-22T00:00:30Z")));

    dateRange = dateRangeProvider.latestDateRange();
    assertThat(dateRange.getFrom()).isEqualTo(OffsetDateTime.parse("2020-04-21T12:00:00Z"));
    assertThat(dateRange.getTo()).isEqualTo(OffsetDateTime.parse("2020-04-22T00:00:00Z"));
  }

  @Test
  void returnsDateRangeOfLast2Hours() {
    String everyEvenHourCronExpression = "0 0 0/2 ? * *";
    DateRangeProvider dateRangeProvider = new DateRangeProvider(
        everyEvenHourCronExpression,
        new MockTimeSource(Instant.parse("2020-04-22T12:01:30Z")));

    DateRange dateRange = dateRangeProvider.latestDateRange();
    assertThat(dateRange.getFrom()).isEqualTo(OffsetDateTime.parse("2020-04-22T10:00:00Z"));
    assertThat(dateRange.getTo()).isEqualTo(OffsetDateTime.parse("2020-04-22T12:00:00Z"));
  }
}
