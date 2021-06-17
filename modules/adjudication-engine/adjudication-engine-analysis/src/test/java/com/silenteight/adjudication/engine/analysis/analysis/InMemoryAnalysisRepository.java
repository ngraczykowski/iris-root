package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

class InMemoryAnalysisRepository extends BasicInMemoryRepository<AnalysisEntity> {

  AnalysisRepository getRepository() {
    return InMemoryAnalysisRepository.this::save;
  }

  AnalysisQueryRepository getQueryRepository() {
    return id -> InMemoryAnalysisRepository.this
        .findById(id)
        .map(a -> AnalysisQuery.builder()
            .analysis(a)
            .alertCount(0L)
            .pendingAlerts(0L)
            .datasetCount(0L)
            .build());
  }
}
