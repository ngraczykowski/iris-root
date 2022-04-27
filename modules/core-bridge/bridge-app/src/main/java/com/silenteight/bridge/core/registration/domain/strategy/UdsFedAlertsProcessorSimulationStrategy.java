package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class UdsFedAlertsProcessorSimulationStrategy implements UdsFedAlertsProcessorStrategy {

  private final AlertRepository alertRepository;

  @Override
  public void processUdsFedAlerts(Batch batch, List<String> alertNames) {
    log.info(
        "UDS fed alerts are being processed for simulation batch with id [{}].", batch.id());
    updateAlertsStatus(batch.id(), alertNames);
  }

  private void updateAlertsStatus(String batchId, List<String> alertNames) {
    log.info(
        "Set [{}] alerts status to [{}] for simulation batch with id [{}].",
        alertNames.size(), AlertStatus.UDS_FED, batchId);
    alertRepository.updateStatusToUdsFed(batchId, alertNames);
  }

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SIMULATION;
  }
}
