package com.silenteight.simulator.processing.alert.index.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface IndexedAlertRepository extends Repository<IndexedAlertEntity, Long> {

  IndexedAlertEntity save(IndexedAlertEntity indexedAlertEntity);

  Optional<IndexedAlertEntity> findByAnalysisName(String analysisName);
}
