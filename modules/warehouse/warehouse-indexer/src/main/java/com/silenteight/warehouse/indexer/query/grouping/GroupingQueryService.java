package com.silenteight.warehouse.indexer.query.grouping;

/**
 * Provides service which groups data.
 */
public interface GroupingQueryService {

  /**
   * Generates data based one the {@code FetchGroupedTimeRangedDataRequest}
   */
  FetchGroupedDataResponse generate(
      FetchGroupedTimeRangedDataRequest fetchGroupedDataRequest);
}
