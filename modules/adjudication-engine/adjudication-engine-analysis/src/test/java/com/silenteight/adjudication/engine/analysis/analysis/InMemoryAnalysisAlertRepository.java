package com.silenteight.adjudication.engine.analysis.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class InMemoryAnalysisAlertRepository implements AnalysisAlertRepository {

  private final List<AnalysisAlertEntity> store = new ArrayList<>();

  @Override
  public Collection<AnalysisAlertEntity> saveAll(Iterable<AnalysisAlertEntity> entities) {
    var list = new ArrayList<AnalysisAlertEntity>();
    entities.forEach(list::add);
    store.addAll(list);
    return list;
  }
}
