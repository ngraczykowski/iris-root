package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertEntity.Status;
import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertName;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
class JdbcAlertRepository implements AlertRepository {

  private final CrudAlertRepository alertRepository;
  private final JdbcAlertMapper mapper;

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
  public void updateStatusToProcessing(String batchId, List<String> alertNames) {
    alertRepository.updateAlertsStatusByBatchIdAndIdsIn(
        batchId, Status.PROCESSING.name(), alertNames);
  }

  @Override
  public void updateStatusToError(
      String batchId, Map<String, Set<String>> errorDescriptionsWithAlertNames) {
    errorDescriptionsWithAlertNames.forEach((errorDescription, alertNames) ->
        alertRepository.updateAlertsStatusWithErrorDescriptionByBatchIdAndAlertNames(
            batchId, alertNames, Status.ERROR.name(), errorDescription));
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
  public List<Alert> findAllByBatchIdAndNameIn(String batchId, List<String> alertNames) {
    return alertRepository.findAllByBatchIdAndNameIn(batchId, alertNames).stream()
        .map(mapper::toAlert)
        .toList();
  }

  @Override
  public long countAllAlerts(String batchId) {
    return alertRepository.countAllAlertsByBatchId(batchId);
  }
}
