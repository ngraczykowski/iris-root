package com.silenteight.warehouse.statistics.aggregators;

import com.silenteight.warehouse.statistics.AggregationPeriod;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Representation of data aggregator based on the buckets and provided models.
 *
 * @param <T>
 *     Model on which aggregation will be executed
 */
public interface DataAggregator<T> {

  /**
   * @param ranges
   *     bucket for models which will be aggregated
   * @param data
   *     list of models
   *
   * @return aggregated data based on the range
   */
  Map<Range<LocalDate>, List<T>> aggregate(Range<LocalDate> ranges, List<T> data);

  /**
   * @return aggregation period
   */
  AggregationPeriod getAggregationPeriod();
}
