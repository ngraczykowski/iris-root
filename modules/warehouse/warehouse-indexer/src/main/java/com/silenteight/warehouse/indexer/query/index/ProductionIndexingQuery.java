package com.silenteight.warehouse.indexer.query.index;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;

import java.util.List;

import static java.util.Collections.singletonList;

@RequiredArgsConstructor
class ProductionIndexingQuery implements IndexesQuery {

  @NonNull
  private final String productionAliasName;

  @Override
  public List<String> getIndexesForAnalysis(String analysisName) {
    return singletonList(productionAliasName);
  }
}
