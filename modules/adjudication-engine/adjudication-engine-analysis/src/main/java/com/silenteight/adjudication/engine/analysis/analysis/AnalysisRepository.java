package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.analysis.domain.StrategyName;

import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;

interface AnalysisRepository extends Repository<AnalysisEntity, Long> {

  @Nonnull
  AnalysisEntity save(AnalysisEntity analysisEntity);

  StrategyName getStrategyById(long analysisId);
}
