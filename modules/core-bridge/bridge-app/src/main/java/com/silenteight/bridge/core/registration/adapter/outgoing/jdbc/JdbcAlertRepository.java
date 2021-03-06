package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertEntity.Status;
import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.apache.commons.collections.CollectionUtils;
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
    if (CollectionUtils.isNotEmpty(alerts)) {
      insertAlerts(alerts);
      insertMatches(alerts);
    }
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

  public List<AlertWithMatches> findAllWithMatchesByBatchIdAndStatusIsNotRegistered(
      String batchId) {
    return mapper.toAlertWithMatches(
        alertRepository.findAllWithMatchesByBatchIdAndNotInStatus(
            batchId, AlertStatus.REGISTERED.name()));
  }

  @Override
  public List<AlertWithMatches> findAllWithMatchesByBatchIdAndNameInAndStatusIsNotRegistered(
      String batchId, List<String> alertNames) {
    return mapper.toAlertWithMatches(
        alertRepository.findAllWithMatchesByBatchIdAndNotInStatusAndAlertNamesIn(
            batchId, AlertStatus.REGISTERED.name(), alertNames));
  }

  @Override
  public Stream<AlertWithoutMatches> streamAllByBatchIdAndStatusIsNotRegistered(String batchId) {
    var params = new MapSqlParameterSource()
        .addValue("batchId", batchId)
        .addValue("status", AlertWithoutMatches.AlertStatus.REGISTERED.name());

    return jdbcTemplate.queryForStream("""
        SELECT
          a.id AS id,
          a.alert_id AS alert_id,
          a.name AS alert_name,
          a.status AS alert_status,
          a.metadata AS alert_metadata,
          a.error_description AS alert_error_description
        FROM core_bridge_alerts a
        WHERE NOT a.is_archived
          AND a.batch_id = :batchId
          AND status != :status
        ORDER BY a.name""", params, new AlertWithoutMatchesRowMapper());
  }

  @Override
  public Stream<AlertWithoutMatches> streamAllByBatchIdAndNameInAndStatusIsNotRegistered(
      String batchId, List<String> alertNames) {
    var params = new MapSqlParameterSource()
        .addValue("batchId", batchId)
        .addValue("alertNames", alertNames)
        .addValue("status", AlertWithoutMatches.AlertStatus.REGISTERED.name());

    return jdbcTemplate.queryForStream("""
        SELECT
          a.id AS id,
          a.alert_id AS alert_id,
          a.name AS alert_name,
          a.status AS alert_status,
          a.metadata AS alert_metadata,
          a.error_description AS alert_error_description
        FROM core_bridge_alerts a
        WHERE NOT a.is_archived
          AND a.batch_id = :batchId
          AND status != :status
          AND a.name IN(:alertNames)
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

  private void insertAlerts(List<Alert> alerts) {
    var params = alerts.stream()
        .map(mapper::toAlertParameters)
        .toArray(MapSqlParameterSource[]::new);
    jdbcTemplate.batchUpdate("""
        INSERT INTO core_bridge_alerts(alert_id, batch_id, name, metadata, status, created_at,
        error_description, alert_time, is_archived)
        VALUES (:alertId, :batchId, :name, :metadata, :status, NOW(), :errorDescription, :alertTime,
        :isArchived);
        """, params);
  }

  private void insertMatches(List<Alert> alerts) {
    var params = alerts.stream()
        .flatMap(alert -> alert.matches().stream()
            .map(match -> mapper.toMatchParameters(alert, match)))
        .toArray(MapSqlParameterSource[]::new);
    jdbcTemplate.batchUpdate("""
        INSERT INTO core_bridge_matches(match_id, alert_id, name, created_at)
        VALUES (:matchId, (SELECT id FROM core_bridge_alerts WHERE alert_id = :alertId
        AND batch_id = :batchId), :name, NOW())
        """, params);
  }
}
