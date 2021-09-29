package com.silenteight.warehouse.indexer.query.index;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.analysis.SimulationNamingStrategy;
import com.silenteight.warehouse.indexer.indexing.IndexesQuery;

import java.util.List;

import static java.util.Collections.singletonList;

@RequiredArgsConstructor
class SimulationIndexingQuery implements IndexesQuery {

  @NonNull
  private final String environmentPrefix;

  @Override
  public List<String> getIndexesForAnalysis(String analysisName) {
    return singletonList(
        new SimulationNamingStrategy(environmentPrefix).getElasticIndexName(analysisName));
  }
}
