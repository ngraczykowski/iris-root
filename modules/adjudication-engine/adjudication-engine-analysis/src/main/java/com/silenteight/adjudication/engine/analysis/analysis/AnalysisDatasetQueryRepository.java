package com.silenteight.adjudication.engine.analysis.analysis;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.stream.Stream;

interface AnalysisDatasetQueryRepository
    extends Repository<AnalysisDatasetQueryEntity, AnalysisDatasetKey> {

  Stream<AnalysisDatasetQueryEntity> findAllByIdIn(Collection<AnalysisDatasetKey> ids);
}
