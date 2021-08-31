package com.silenteight.adjudication.engine.analysis.analysis;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface AnalysisDatasetRepository extends Repository<AnalysisDatasetEntity, AnalysisDatasetKey> {

  Collection<AnalysisDatasetEntity> saveAll(Iterable<AnalysisDatasetEntity> entity);
}
