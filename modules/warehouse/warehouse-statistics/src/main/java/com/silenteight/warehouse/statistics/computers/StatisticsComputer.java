package com.silenteight.warehouse.statistics.computers;

import java.util.List;

/**
 * Representation of statistics data computer which can be used by {@code StatisticDataCollector}
 *
 * @param <S>
 *     Model represents a data which will be computed.
 * @param <T>
 *     Computed data based on list of models.
 */
public interface StatisticsComputer<S, T> {

  /**
   * Compute data from model S to provide statistics on defined set.
   *
   * @param data
   *     on which compute will happen
   *
   * @return computed data
   */
  T compute(List<S> data);
}
