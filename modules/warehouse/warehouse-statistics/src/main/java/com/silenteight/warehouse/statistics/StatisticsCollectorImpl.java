package com.silenteight.warehouse.statistics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.statistics.aggregators.DataAggregator;
import com.silenteight.warehouse.statistics.computers.StatisticsComputer;
import com.silenteight.warehouse.statistics.extractors.DataExtractor;
import com.silenteight.warehouse.statistics.persistance.DataPersister;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Collect data based on the last LocalDate from {@code DailyRecommendationStatistics}
 *
 * <p>If there is no data in {@code DailyRecommendationStatistics} table as the reference point the
 * first model is taken from {@code DataExtractor}
 *
 * <p>Historical data used by aggregator can be changed as data provided also sends updates to our
 * system. Because of that we can not calculate only data for current period, but we also need to
 * recalculate them for statistics in previous periods.
 */
@RequiredArgsConstructor
@Slf4j
public final class StatisticsCollectorImpl<T, S> implements StatisticsCollector {

  @NonNull
  private final TimeSource timeSource;
  @NonNull
  private final DataExtractor<T> dataExtractor;
  @NonNull
  private final DataAggregator<T> dataAggregator;
  @NonNull
  private final StatisticsComputer<T, S> dataComputer;
  @NonNull
  private final DataPersister<S> dataPersister;
  @NonNull
  private final Number recalculationPeriod;
  @NonNull
  private final Integer dataRangeBucketSize;

  public void generateStatisticsData() {

    LocalDate startingDate = dataPersister.getLastPersistedRange(recalculationPeriod)
        .map(Range::upperEndpoint)
        .orElseGet(dataExtractor::getEarliestDate);

    // We are collecting data from day before to make sure we have all data for desired period
    var oneDayBeforeNow = timeSource.localDateTime().toLocalDate().minusDays(1);
    var collectorRange = Range.closed(startingDate, oneDayBeforeNow);
    List<Range<LocalDate>> collectorRangeBuckets = createRangeBuckets(collectorRange);

    collectorRangeBuckets.forEach(this::execution);
  }

  private List<Range<LocalDate>> createRangeBuckets(Range<LocalDate> collectorRange) {
    LocalDate starDateForBucket = collectorRange.lowerEndpoint();
    LocalDate endDateForBucket;
    var buckets = new ArrayList<Range<LocalDate>>();
    while (collectorRange.contains(starDateForBucket)) {
      endDateForBucket =
          starDateForBucket.plusDays(
              (long) dataAggregator.getAggregationPeriod().getNumberOfDays() * dataRangeBucketSize);
      if (endDateForBucket.isAfter(collectorRange.upperEndpoint())) {
        buckets.add(Range.closed(starDateForBucket, collectorRange.upperEndpoint()));
        break;
      }
      buckets.add(Range.closed(starDateForBucket, endDateForBucket));
      starDateForBucket = endDateForBucket.plusDays(1);
    }
    return buckets;
  }

  private void execution(Range<LocalDate> range) {
    log.info("Calculation will be made on date range {}", range);
    var extractedData = dataExtractor.getData(range);

    log.debug("Number of models taken to the calculation {}", extractedData.size());
    Map<Range<LocalDate>, List<T>> result =
        dataAggregator.aggregate(range, extractedData);

    for (Entry<Range<LocalDate>, List<T>> entry : result.entrySet()) {
      var computedData = dataComputer.compute(entry.getValue());
      log.info("Computation {} was made for range {}", computedData, entry.getKey());
      dataPersister.save(computedData, entry.getKey());
    }

    log.info("Statistics calculation for range {} finished", range);
  }
}
