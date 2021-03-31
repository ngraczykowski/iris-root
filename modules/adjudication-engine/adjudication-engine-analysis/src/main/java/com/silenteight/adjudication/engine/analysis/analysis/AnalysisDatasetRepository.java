package com.silenteight.adjudication.engine.analysis.analysis;

import org.springframework.data.repository.Repository;

interface AnalysisDatasetRepository extends Repository<AnalysisDatasetEntity, AnalysisDatasetKey> {

  AnalysisDatasetEntity save(AnalysisDatasetEntity entity);
}
