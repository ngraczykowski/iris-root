package com.silenteight.warehouse.statistics.aggregators;

import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.statistics.AggregationPeriod;

import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AlertAggregatorTest {

  private AlertAggregator aggregator;

  @Test
  void createDailyAggregations() {
    // Given
    aggregator = new AlertAggregator(AggregationPeriod.DAILY);

    // When
    Map<Range<LocalDate>, List<AlertDto>> result = aggregator.aggregate(
        Range.closed(
            LocalDate.of(2022, 4, 1),
            LocalDate.of(2022, 4, 3)),
        List.of());

    // Then
    assertThat(result).containsKeys(
        Range.closed(
            LocalDate.of(2022, 4, 1),
            LocalDate.of(2022, 4, 1)),
        Range.closed(
            LocalDate.of(2022, 4, 2),
            LocalDate.of(2022, 4, 2)),
        Range.closed(
            LocalDate.of(2022, 4, 3),
            LocalDate.of(2022, 4, 3))
    );
  }

  @Test
  void createWeeklyAggregations() {
    // Given
    aggregator = new AlertAggregator(AggregationPeriod.WEEKLY);

    // When
    Map<Range<LocalDate>, List<AlertDto>> result = aggregator.aggregate(
        Range.closed(
            LocalDate.of(2022, 4, 1),
            LocalDate.of(2022, 4, 8)),
        List.of());

    // Then
    assertThat(result).containsKeys(
        Range.closed(
            LocalDate.of(2022, 4, 1),
            LocalDate.of(2022, 4, 7)),
        Range.closed(
            LocalDate.of(2022, 4, 8),
            LocalDate.of(2022, 4, 14)
        )
    );
  }

  @Test
  void createWeeklyAggregationsWithData() {
    // Given
    aggregator = new AlertAggregator(AggregationPeriod.DAILY);
    var alert1 = alertDtoBuilder(
        Timestamp.valueOf("2022-04-01 09:01:15"));
    var alert2 = alertDtoBuilder(
        Timestamp.valueOf("2022-04-02 09:01:15"));

    // When
    Map<Range<LocalDate>, List<AlertDto>> result = aggregator.aggregate(
        Range.closed(
            LocalDate.of(2022, 4, 1),
            LocalDate.of(2022, 4, 2)),
        List.of(alert1, alert2));

    // Then
    assertThat(result).containsExactlyInAnyOrderEntriesOf(
        Map.of(
            Range.closed(
                LocalDate.of(2022, 4, 1),
                LocalDate.of(2022, 4, 1)),
            List.of(alert1),
            Range.closed(
                LocalDate.of(2022, 4, 2),
                LocalDate.of(2022, 4, 2)),
            List.of(alert2)));
  }

  private static AlertDto alertDtoBuilder(Timestamp date) {
    return AlertDto
        .builder()
        .id(1L)
        .name("alertName")
        .discriminator("test_desc")
        .createdAt(date)
        .recommendationDate(date)
        .payload(Map.of())
        .build();
  }
}