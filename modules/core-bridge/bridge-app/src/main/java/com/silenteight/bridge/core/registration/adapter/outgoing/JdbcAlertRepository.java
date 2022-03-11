package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.adapter.outgoing.AlertEntity.Status;
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
  public void updateStatusToProcessing(String batchId, List<String> alertIds) {
    alertRepository.updateAlertsStatusByBatchIdAndIdsIn(
        batchId, Status.PROCESSING.name(), alertIds);
  }

  @Override
  public void updateStatusToError(
      String batchId, Map<String, Set<String>> errorDescriptionsWithAlertIds) {
    errorDescriptionsWithAlertIds.forEach((errorDescription, alertIds) ->
        alertRepository.updateAlertsStatusWithErrorDescriptionByBatchIdAndAlertIds(
            batchId, alertIds, Status.ERROR.name(), errorDescription)
    );
  }

  @Override
  public List<AlertName> findAllAlertNamesByBatchIdAndAlertIdIn(
      String batchId, List<String> alertIds) {
    return alertRepository.findNamesByBatchIdAndAlertIdIn(batchId, alertIds).stream()
        .map(mapper::toAlertName)
        .toList();
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
  public long countAllPendingAlerts(String batchId) {
    return alertRepository.countAllAlertsByBatchIdAndNotRecommendedAndNotErrorStatuses(batchId);
  }

  @Override
  public long countAllErroneousAlerts(String batchId) {
    return alertRepository.countAllAlertsByBatchIdAndErrorStatus(batchId);
  }

  @Override
  public List<Alert> findAllByBatchIdAndAlertIdIn(String batchId, List<String> alertIds) {
    return alertRepository.findAllByBatchIdAndAlertIdIn(batchId, alertIds).stream()
        .map(mapper::toAlert)
        .toList();
  }
}
