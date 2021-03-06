package com.silenteight.hsbc.bridge.analysis;

import com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

interface AnalysisRepository extends Repository<AnalysisEntity, Long> {

  void save(AnalysisEntity analysisEntity);

  List<AnalysisEntity> findByTimeoutAtBeforeAndStatus(OffsetDateTime timeout, Status status);

  Optional<AnalysisEntity> findByName(String name);
}
