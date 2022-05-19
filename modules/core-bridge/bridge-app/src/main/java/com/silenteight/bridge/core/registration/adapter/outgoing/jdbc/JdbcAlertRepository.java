package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertEntity.Status;
import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
class JdbcAlertRepository implements AlertRepository {

  private final CrudAlertRepository alertRepository;
  private final JdbcAlertMapper mapper;
  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public void saveAlerts(List<Alert> alerts) {
    var alertEntities = alerts.stream()
        .map(mapper::toAlertEntity)
        .toList();
    alertRepository.saveAll(alertEntities);
  }

  @Override
  public void updateStatusToRecommended(String batchId, List<String> alertNames) {
    alertRepository.updateAlertsStatusByBatchIdAndNamesIn(
        batchId, Status.RECOMMENDED.name(), alertNames);
  }

  @Override
  public void updateStatusToProcessing(
      String batchId, List<String> alertNames, EnumSet<AlertStatus> statusesNotIn) {
    alertRepository.updateAlertsStatusByBatchIdAndAlertNamesInAndStatusNotIn(
        batchId, Status.PROCESSING.name(), alertNames,
        statusesNotIn.stream().map(Enum::name).toList()
    );
  }

  @Override
  public void updateStatusToUdsFed(String batchId, List<String> alertNames) {
    alertRepository.updateAlertsStatusByBatchIdAndIdsIn(
        batchId, Status.UDS_FED.name(), alertNames);
  }

  @Override
  public void updateStatusToError(
      String batchId, Map<String, Set<String>> errorDescriptionsWithAlertNames,
      EnumSet<AlertStatus> statusesNotIn) {
    errorDescriptionsWithAlertNames.forEach((errorDescription, alertNames) ->
        alertRepository.updateAlertsStatusWithDescriptionByBatchIdAndAlertNamesInAndStatusesNotIn(
            batchId, alertNames, Status.ERROR.name(), errorDescription,
            statusesNotIn.stream().map(Enum::name).toList()));
  }

  @Override
  public void updateStatusToDelivered(String batchId, List<String> alertNames) {
    alertRepository.updateAlertsStatusByBatchIdAndNamesIn(
        batchId, Status.DELIVERED.name(), alertNames);
  }

  @Override
  public void updateStatusToDelivered(String batchId) {
    alertRepository.updateAlertsStatusByBatchId(batchId, Status.DELIVERED.name());
  }

  @Override
  public List<AlertName> findNamesByBatchIdAndStatusIsRegisteredOrProcessing(String batchId) {
    return alertRepository.findNamesByBatchIdAndStatusIsRegisteredOrProcessing(batchId).stream()
        .map(mapper::toAlertName)
        .toList();
  }

  @Override
  public List<Alert> findAllByBatchId(String batchId) {
    return alertRepository.findAllByBatchId(batchId).stream()
        .map(mapper::toAlert)
        .toList();
  }

  @Override
  public List<AlertWithMatches> findAllWithMatchesByBatchId(String batchId) {
    return mapper.toAlertWithMatches(alertRepository.findAllWithMatchesByBatchId(batchId));
  }

  @Override
  public List<AlertWithMatches> findAllWithMatchesByBatchIdAndNameIn(
      String batchId, List<String> alertNames) {
    return mapper.toAlertWithMatches(
        alertRepository.findAllWithMatchesByBatchIdAndAlertNamesIn(batchId, alertNames));
  }

  @Override
  public Stream<AlertWithoutMatches> streamAllByBatchId(String batchId) {
    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("batchId", batchId);

    return jdbcTemplate.queryForStream("""
        SELECT 
          a.id AS id,
          a.alert_id AS alert_id,
          a.name AS alert_name, 
          a.status AS alert_status, 
          a.metadata AS alert_metadata, 
          a.error_description AS alert_error_description
        FROM core_bridge_alerts a
        WHERE a.batch_id = :batchId
        ORDER BY a.name""", params, new AlertWithoutMatchesRowMapper());
  }

  @Override
  public Stream<AlertWithoutMatches> streamAllByBatchIdAndNameIn(
      String batchId, List<String> alertNames) {
    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("batchId", batchId)
        .addValue("alertNames", alertNames);

    return jdbcTemplate.queryForStream("""
        SELECT 
          a.id AS id,
          a.alert_id AS alert_id,
          a.name AS alert_name, 
          a.status AS alert_status, 
          a.metadata AS alert_metadata, 
          a.error_description AS alert_error_description
        FROM core_bridge_alerts a
        WHERE a.batch_id = :batchId AND a.name IN(:alertNames)
        ORDER BY a.name""", params, new AlertWithoutMatchesRowMapper());
  }

  @Override
  public List<MatchWithAlertId> findMatchesByAlertIdIn(Set<Long> alertIds) {
    return jdbcTemplate.query("""
        SELECT
          m.alert_id AS alert_id,
          m.match_id AS match_id,
          m.name AS match_name
        FROM core_bridge_matches m
        WHERE alert_id IN (:alertsIds)
        """, new MapSqlParameterSource("alertsIds", alertIds), new MatchWithAlertNameRowMapper());
  }

  @Override
  public List<AlertWithMatches> findAllWithMatchesByBatchIdAndAlertIdsIn(
      String batchId, List<String> alertIds) {
    return mapper.toAlertWithMatches(
        alertRepository.findAllWithMatchesByBatchIdAndAlertIdsIn(batchId, alertIds));
  }

  @Override
  public long countAllCompleted(String batchId) {
    return alertRepository.countAllAlertsByBatchIdAndStatusIn(
        batchId, Set.of(Status.RECOMMENDED.name(), Status.ERROR.name(), Status.DELIVERED.name()));
  }

  @Override
  public long countAllErroneousAlerts(String batchId) {
    var statuses = Set.of(Status.ERROR.name());
    return alertRepository.countAllAlertsByBatchIdAndStatusIn(batchId, statuses);
  }

  @Override
  public long countAllDeliveredAndErrorAlerts(String batchId) {
    var statuses = Set.of(Status.DELIVERED.name(), Status.ERROR.name());
    return alertRepository.countAllAlertsByBatchIdAndStatusIn(batchId, statuses);
  }

  @Override
  public long countAllUdsFedAndErrorAlerts(String batchId) {
    var statuses = Set.of(Status.UDS_FED.name(), Status.ERROR.name());
    return alertRepository.countAllAlertsByBatchIdAndStatusIn(batchId, statuses);
  }

  @Override
  public List<Alert> findAllByBatchIdAndNameIn(String batchId, List<String> alertNames) {
    return alertRepository.findAllByBatchIdAndNameIn(batchId, alertNames).stream()
        .map(mapper::toAlert)
        .toList();
  }

  @Override
  public long countAllAlerts(String batchId) {
    return alertRepository.countAllAlertsByBatchId(batchId);
  }

  @Override
  public List<AlertToRetention> findAlertsApplicableForDataRetention(Instant expirationDate) {
    return alertRepository.findAllByAlertTimeBefore(expirationDate).stream()
        .map(mapper::toAlertToRetention)
        .toList();
  }

  @Override
  public void markAsArchivedAndClearMetadata(List<Long> alertPrimaryIds) {
    alertRepository.markAsArchivedAndClearMetadata(alertPrimaryIds);
  }
}
