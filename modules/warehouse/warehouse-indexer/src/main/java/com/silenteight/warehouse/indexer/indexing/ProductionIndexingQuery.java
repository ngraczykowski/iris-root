package com.silenteight.warehouse.indexer.indexing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.analysis.ProductionNamingStrategy;

import java.util.List;

import static java.util.Collections.singletonList;

@RequiredArgsConstructor
class ProductionIndexingQuery implements IndexesQuery {

  @NonNull
  private final String environmentPrefix;

  @Override
  public List<String> getIndexesForAnalysis(String analysisName) {
    return singletonList(new ProductionNamingStrategy(environmentPrefix).getElasticIndexName());
  }
}
