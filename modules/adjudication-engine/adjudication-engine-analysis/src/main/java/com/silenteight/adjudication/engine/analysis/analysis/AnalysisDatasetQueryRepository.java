package com.silenteight.adjudication.engine.analysis.analysis;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AnalysisDatasetQueryRepository
    extends Repository<AnalysisDatasetQueryEntity, AnalysisDatasetKey> {

  Optional<AnalysisDatasetQueryEntity> findById(AnalysisDatasetKey id);
}
