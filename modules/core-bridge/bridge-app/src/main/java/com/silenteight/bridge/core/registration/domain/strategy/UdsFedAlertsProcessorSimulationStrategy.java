package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.SimulationBatchCompleted;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class UdsFedAlertsProcessorSimulationStrategy implements UdsFedAlertsProcessorStrategy {

  private final AlertRepository alertRepository;
  private final BatchRepository batchRepository;
  private final BatchEventPublisher eventPublisher;

  @Override
  public void processUdsFedAlerts(Batch batch, List<String> alertNames) {
    log.info(
        "UDS fed alerts are being processed for simulation batch with id [{}].", batch.id());
    updateAlertsStatus(batch.id(), alertNames);
    var udsFedAndErrorAlertsCount = alertRepository.countAllUdsFedAndErrorAlerts(batch.id());
    if (batch.alertsCount() == udsFedAndErrorAlertsCount) {
      log.info(
          "All [{}] alerts have either [{}] or [{}] status for simulation batch with id [{}].",
          udsFedAndErrorAlertsCount, AlertStatus.UDS_FED, AlertStatus.ERROR, batch.id());
      markSimulationBatchAsCompleted(batch.id());
      publishSimulationBatchCompleted(batch);
    }
  }

  private void updateAlertsStatus(String batchId, List<String> alertNames) {
    log.info(
        "Set [{}] alerts status to [{}] for simulation batch with id [{}].",
        alertNames.size(), AlertStatus.UDS_FED, batchId);
    alertRepository.updateStatusToUdsFed(batchId, alertNames);
  }

  private void markSimulationBatchAsCompleted(String batchId) {
    log.info(
        "Set simulation batch status to [{}] with id [{}].",
        BatchStatus.COMPLETED, batchId);
    batchRepository.updateStatusToCompleted(batchId);
  }

  private void publishSimulationBatchCompleted(Batch batch) {
    eventPublisher.publish(SimulationBatchCompleted.builder()
        .id(batch.id())
        .analysisName(batch.analysisName())
        .batchMetadata(batch.batchMetadata())
        .build()
    );
  }

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SIMULATION;
  }
}
