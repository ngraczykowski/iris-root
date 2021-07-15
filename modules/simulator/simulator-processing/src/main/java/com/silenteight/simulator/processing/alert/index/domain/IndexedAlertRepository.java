package com.silenteight.simulator.processing.alert.index.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

interface IndexedAlertRepository extends Repository<IndexedAlertEntity, Long> {

  IndexedAlertEntity save(IndexedAlertEntity indexedAlertEntity);

  Optional<IndexedAlertEntity> findByAnalysisName(String analysisName);

  Collection<IndexedAlertEntity> findAllByAnalysisName(String analysisName);

  Optional<IndexedAlertEntity> findByRequestId(String requestId);

  @Query(value = "SELECT s.analysis_name"
      + " FROM simulator_indexed_alert s"
      + " WHERE s.request_id = :requestId", nativeQuery = true)
  Optional<String> findAnalysisNameUsingRequestId(String requestId);

  @Query(value = "SELECT COUNT(*)"
      + " FROM simulator_indexed_alert s"
      + " WHERE s.state != 'ACKED'"
      + " AND s.analysis_name = :analysisName", nativeQuery = true)
  long getAmountOfNonAckedWithAnalysisName(String analysisName);

  @Query(value = "SELECT SUM(s.alert_count)"
      + " FROM simulator_indexed_alert s"
      + " WHERE s.analysis_name = :analysisName", nativeQuery = true)
  long sumAllAlertsCountWithAnalysisName(String analysisName);
}
