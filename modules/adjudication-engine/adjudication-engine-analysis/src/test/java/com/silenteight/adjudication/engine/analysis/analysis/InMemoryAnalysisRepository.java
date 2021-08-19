package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.analysis.domain.StrategyName;
import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.jetbrains.annotations.NotNull;

class InMemoryAnalysisRepository extends BasicInMemoryRepository<AnalysisEntity>
    implements AnalysisRepository {

  AnalysisRepository getRepository() {
    return this;
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

  @NotNull
  @Override
  public AnalysisEntity save(
      AnalysisEntity analysisEntity) {
    return super.save(analysisEntity);
  }

  @Override
  public StrategyName getStrategyById(long analysisId) {
    return null;
  }
}
