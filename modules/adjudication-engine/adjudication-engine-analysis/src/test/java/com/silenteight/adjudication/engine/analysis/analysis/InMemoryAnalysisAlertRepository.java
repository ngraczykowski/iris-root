package com.silenteight.adjudication.engine.analysis.analysis;

import java.util.ArrayList;
import java.util.List;

class InMemoryAnalysisAlertRepository implements AnalysisAlertRepository {

  private final List<AnalysisAlertEntity> store = new ArrayList<>();

  @Override
  public AnalysisAlertEntity save(AnalysisAlertEntity entity) {
    store.add(entity);
    return entity;
  }
}
