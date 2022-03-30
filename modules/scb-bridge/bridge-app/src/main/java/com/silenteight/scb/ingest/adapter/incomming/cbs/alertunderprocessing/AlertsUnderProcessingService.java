package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.min;

@RequiredArgsConstructor
class AlertsUnderProcessingService implements AlertInFlightService {

  private final AlertUnderProcessingRepository alertUnderProcessingRepository;

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void saveUniqueAlerts(
      Collection<AlertId> alerts, String internalBatchId, ScbAlertIdContext alertIdContext) {
    Collection<AlertId> alertsUnderProcessing = getAlertsUnderProcessing(alerts);
    List<AlertId> alertsToBeSaved = alerts
        .stream()
        .filter(a -> !alertsUnderProcessing.contains(a))
        .toList();
    saveAlertsToBeProcess(alertsToBeSaved, internalBatchId, alertIdContext);
  }

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void delete(@NonNull AlertId alertId) {
    alertUnderProcessingRepository.deleteBySystemIdAndBatchId(
        alertId.getSystemId(), alertId.getBatchId());
  }

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void deleteExpired(@NonNull OffsetDateTime expireDate) {
    alertUnderProcessingRepository.deleteByCreatedAtBefore(expireDate);
  }

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void update(@NonNull AlertId alertId, @NonNull AlertUnderProcessing.State state) {
    alertUnderProcessingRepository.update(alertId.getSystemId(), alertId.getBatchId(), state);
  }

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void update(
      @NonNull AlertId alertId, @NonNull AlertUnderProcessing.State state, @NonNull String error) {
    String trimmedError = error.substring(0, min(error.length(), 1000));

    alertUnderProcessingRepository.update(
        alertId.getSystemId(), alertId.getBatchId(), state, trimmedError);
  }

  @Override
  public List<AlertIdWithDetails> getAlertsFromBatch(String internalBatchId) {
    return alertUnderProcessingRepository.findAllByInternalBatchId(internalBatchId)
        .stream()
        .map(this::getAlertIdWithDetails)
        .toList();
  }

  private AlertIdWithDetails getAlertIdWithDetails(AlertUnderProcessing alert) {
    try {
      return AlertIdWithDetails
          .builder()
          .systemId(alert.getSystemId())
          .batchId(alert.getBatchId())
          .context(ScbAlertIdContext.parseFrom(alert.getPayload()))
          .build();
    } catch (InvalidProtocolBufferException exception) {
      throw new IllegalStateException("Invalid proto definition", exception);
    }
  }

  private Collection<AlertId> getAlertsUnderProcessing(Collection<AlertId> alerts) {
    var systemIds = alerts.stream().map(AlertId::getSystemId).toList();
    return alertUnderProcessingRepository
        .findAllBySystemIdIn(systemIds)
        .stream()
        .map(a -> AlertId.builder().systemId(a.getSystemId()).batchId(a.getBatchId()).build())
        .toList();
  }

  private void saveAlertsToBeProcess(
      Collection<AlertId> alerts, String internalBatchId, ScbAlertIdContext alertIdContext) {
    alertUnderProcessingRepository
        .saveAll(alerts.stream()
            .map(alert -> toEntity(alert, internalBatchId, alertIdContext))
            .toList());
  }

  private AlertUnderProcessing toEntity(
      AlertId alertId, String internalBatchId, ScbAlertIdContext scbAlertIdContext) {
    return new AlertUnderProcessing(
        alertId.getSystemId(),
        alertId.getBatchId(),
        internalBatchId,
        scbAlertIdContext);
  }
}