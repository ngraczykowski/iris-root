package com.silenteight.warehouse.statistics.aggregators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.statistics.AggregationPeriod;

import com.google.common.collect.Range;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Aggregator for alerts used to calculate statistics based on the recommendationDate and desired
 * range.
 *
 * <p>Buckets used for aggregation are created based on {@code AggregationRangeSize}
 */
@RequiredArgsConstructor
public final class AlertAggregator implements DataAggregator<AlertDto> {

  @NonNull
  private final AggregationPeriod rangeSize;

  @Override
  public Map<Range<LocalDate>, List<AlertDto>> aggregate(
      Range<LocalDate> range, List<AlertDto> data) {
    Map<Range<LocalDate>, List<AlertDto>> aggregatedRanges = generateRangesToAggregator(range);

    for (AlertDto alertDto : data) {
      var alertLocalDate =
          alertDto
              .getRecommendationDate()
              .toInstant()
              .atZone(ZoneOffset.UTC)
              .toLocalDate();
      for (Entry<Range<LocalDate>, List<AlertDto>> entry : aggregatedRanges.entrySet()) {
        if (entry.getKey().contains(alertLocalDate)) {
          entry.getValue().add(alertDto);
          break;
        }
      }
    }
    return aggregatedRanges;
  }

  @Override
  public AggregationPeriod getAggregationPeriod() {
    return rangeSize;
  }

  @NotNull
  private Map<Range<LocalDate>, List<AlertDto>> generateRangesToAggregator(
      Range<LocalDate> dateRange) {
    var diffRangeInDays = rangeSize.getNumberOfDays() - 1;
    var lowerLocalDate = dateRange.lowerEndpoint();
    var upperLocalDate = lowerLocalDate.plusDays(diffRangeInDays);

    Map<Range<LocalDate>, List<AlertDto>> result = new HashMap<>();

    while (dateRange.contains(lowerLocalDate)) {
      result.put(Range.closed(lowerLocalDate, upperLocalDate), new ArrayList<>());
      lowerLocalDate = upperLocalDate.plusDays(1);
      upperLocalDate = lowerLocalDate.plusDays(diffRangeInDays);
    }
    return result;
  }
}
