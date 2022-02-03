package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.indexer.alert.dto.AlertGroupingDto;

import com.google.common.collect.ListMultimap;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Provides access to alert repository.
 */
public interface AlertRepository {

  /**
   * Fetches random {@link AlertDto}s based on the filters and provided conditions. Method allows
   * filtering out results based on {@code filters} property.
   *
   * <p>e.g. of {@code filters} structure
   * [ - filter_name: recommendation - values:FALSE_POSITIVE, INVESTIGATE, - filter_name: riskType -
   * values: PEP it tells us to fetch only alerts where recommendation is FALSE_POSITIVE or
   * INVESTIGATE and riskType is PEP
   *
   * @param alertColumnName
   *     name of the alertDateColumns which will be used as property name
   * @param timeFrom
   *     time from
   * @param timeTo
   *     time to
   * @param limit
   *     limit of fetched {@link AlertDto}
   * @param filters
   *     filter which will be used to filer out {@link AlertDto}
   * @param alertNames
   *     alert names
   *
   * @return list of {@link AlertDto}
   */
  List<AlertDto> fetchRandomAlerts(
      AlertColumnName alertColumnName, String timeFrom, String timeTo, int limit,
      ListMultimap<String, List<String>> filters, List<String> alertNames);

  /**
   * Fetches alerts grouped by provided {@code groupByFields}. Additionally, alerts are filter out
   * by time range and {@code filters}
   *
   * @param alertColumnName
   *     name of the alertDateColumns which will be used as property name
   * @param timeFrom
   *     time from
   * @param timeTo
   *     time to
   * @param filters
   *     filter which will be used to filer out {@link AlertGroupingDto}
   * @param groupByFields
   *     list of fields to group by {@link AlertGroupingDto}
   */
  List<AlertGroupingDto> fetchGroupedAlerts(
      AlertColumnName alertColumnName, OffsetDateTime timeFrom, OffsetDateTime timeTo,
      ListMultimap<String, List<String>> filters, List<String> groupByFields);
}
