package com.silenteight.simulator.processing.alert.index.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface IndexedAlertRepository extends Repository<IndexedAlertEntity, Long> {

  IndexedAlertEntity save(IndexedAlertEntity indexedAlertEntity);

  Optional<IndexedAlertEntity> findByAnalysisName(String analysisName);

  Collection<IndexedAlertEntity> findAllByAnalysisName(String analysisName);

  Optional<IndexedAlertEntity> findByRequestId(String requestId);

  long countAllByAnalysisNameAndStateIn(String analysisName, List<State> states);

  @Query(value = "SELECT s.analysis_name"
      + " FROM simulator_indexed_alert s"
      + " WHERE s.request_id = :requestId", nativeQuery = true)
  Optional<String> findAnalysisNameUsingRequestId(String requestId);

  @Query(value = "SELECT SUM(s.alertCount)"
      + " FROM IndexedAlertEntity s"
      + " WHERE s.analysisName = :analysisName"
      + " AND s.state IN :states")
  long sumAllAlertsCountWithAnalysisName(String analysisName,Collection<State> states);
}
