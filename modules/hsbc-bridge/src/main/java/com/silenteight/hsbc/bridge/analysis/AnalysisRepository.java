package com.silenteight.hsbc.bridge.analysis;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AnalysisRepository extends Repository<AnalysisEntity, Long> {

  void save(AnalysisEntity analysisEntity);

  Optional<AnalysisEntity> findById(Long id);
}
