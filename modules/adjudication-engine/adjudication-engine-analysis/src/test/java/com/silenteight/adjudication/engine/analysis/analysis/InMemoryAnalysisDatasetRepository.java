package com.silenteight.adjudication.engine.analysis.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class InMemoryAnalysisDatasetRepository implements AnalysisDatasetRepository {

  private final List<AnalysisDatasetEntity> analysisDatasetEntities = new ArrayList<>();

  public List<AnalysisDatasetEntity> getAnalysisDatasetList() {
    return Collections.unmodifiableList(analysisDatasetEntities);
  }

  @Override
  public AnalysisDatasetEntity save(AnalysisDatasetEntity analysisDatasetEntity) {
    analysisDatasetEntities.add(analysisDatasetEntity);
    return analysisDatasetEntity;
  }
}
