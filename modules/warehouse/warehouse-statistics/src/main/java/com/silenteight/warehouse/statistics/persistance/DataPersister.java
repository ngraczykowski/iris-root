package com.silenteight.warehouse.statistics.persistance;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Representation of persistance layer which stores calculated models.
 *
 * @param <T>
 *     Model representation of persistand data
 */
public interface DataPersister<T> {

  /**
   * Saves statistical data for desired range
   *
   * @param data
   *     model representation for data to save
   * @param range
   *     date of the represented model
   */
  void save(T data, Range<LocalDate> range);

  /**
   * Gets last range for persisted model. When there is no model representation it returns
   * Optional.empty
   *
   * @return rage for last persisted model if exists if not empty
   */
  Optional<Range<LocalDate>> getLastPersistedRange(Number value);
}
