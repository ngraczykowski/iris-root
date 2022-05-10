package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static java.lang.Math.min;

@RequiredArgsConstructor
class AlertsUnderProcessingService implements AlertInFlightService {

  private final AlertUnderProcessingRepository alertUnderProcessingRepository;
  private final AlertUnderProcessingProperties alertUnderProcessingProperties;

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void saveUniqueAlerts(Collection<AlertId> alerts, ScbAlertIdContext alertIdContext) {
    Collection<AlertId> alertsUnderProcessing = getAlertsUnderProcessing(alerts);
    List<AlertId> alertsToBeSaved = alerts.stream()
        .filter(a -> !alertsUnderProcessing.contains(a))
        .toList();
    saveAlertsToBeProcess(alertsToBeSaved, alertIdContext);
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
    alertUnderProcessingRepository.deleteByCreatedAtBeforeAndStateIn(expireDate, EnumSet.of(
        State.ERROR,
        State.ACK));
  }

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void deleteAlerts(List<AlertId> alertIds) {
    alertIds.forEach(this::delete);
  }

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void ack(@NonNull AlertId alertId) {
    alertUnderProcessingRepository.update(alertId.getSystemId(), alertId.getBatchId(), State.ACK);
  }

  @Override
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void error(@NonNull AlertId alertId, @NonNull String error) {
    String trimmedError = error.substring(0, min(error.length(), 1000));

    alertUnderProcessingRepository.update(
        alertId.getSystemId(), alertId.getBatchId(), State.ERROR, trimmedError);
  }

  @Override
  public List<AlertIdWithDetails> readChunk() {
    return alertUnderProcessingRepository.findTopNByStateOrderByPriorityDesc(
            State.IN_PROGRESS, alertUnderProcessingProperties.readChunkSize())
        .stream()
        .map(this::getAlertIdWithDetails)
        .toList();
  }

  @Override
  public long getAckAlertsCount() {
    return alertUnderProcessingRepository.countByState(State.ACK);
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
    return alertUnderProcessingRepository.findAllBySystemIdIn(systemIds);
  }

  private void saveAlertsToBeProcess(Collection<AlertId> alerts, ScbAlertIdContext alertIdContext) {
    alertUnderProcessingRepository
        .saveAll(alerts.stream()
            .map(alert -> toEntity(alert, alertIdContext))
            .toList());
  }

  private AlertUnderProcessing toEntity(AlertId alertId, ScbAlertIdContext scbAlertIdContext) {
    return new AlertUnderProcessing(
        alertId.getSystemId(),
        alertId.getBatchId(),
        scbAlertIdContext);
  }
}
