package com.silenteight.warehouse.statistics.extractors;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.List;

/**
 * Extractor which provides a way to provide data to do statistical calculation.
 *
 * @param <T>
 *     Model on which calculation will be done to get statistics data.
 */
public interface DataExtractor<T> {

  /**
   * Gets the initial date of the stored model.
   */
  LocalDate getEarliestDate();

  /**
   * Gets data for desired date range
   *
   * @param range
   *     range od date
   *
   * @return list of available models
   */
  List<T> getData(Range<LocalDate> range);
}
