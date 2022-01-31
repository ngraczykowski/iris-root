package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.indexer.alert.AlertRepository;

public class GroupingQueryPostgresService implements GroupingQueryService {

  public GroupingQueryPostgresService(AlertRepository alertRepository) {
  }

  @Override
  public FetchGroupedDataResponse generate(
      FetchGroupedTimeRangedDataRequest fetchGroupedDataRequest) {
    //TODO(tdrozdz): implement it in follow up MR
    return null;
  }
}
