package com.silenteight.warehouse.indexer.query.single;

import lombok.NoArgsConstructor;

import com.google.common.collect.ImmutableList;

import java.util.List;

@NoArgsConstructor
public class RandomPostgresSearchAlertQueryService implements RandomAlertService {

  @Override
  public List<String> getRandomAlertNameByCriteria(AlertSearchCriteria alertsSampleRequest) {
    // TODO(tdrozdz): Add proper implementation to fetch random alerts based on request from
    // postgres
    return ImmutableList.of();
  }
}
