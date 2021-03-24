package com.silenteight.hsbc.bridge.analysis;

import org.springframework.data.repository.Repository;

interface AnalysisRepository extends Repository<AnalysisEntity, Long> {

  void save(AnalysisEntity analysisEntity);
}
