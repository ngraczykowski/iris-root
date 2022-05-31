package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.infrastructure.application.RegistrationAnalysisProperties;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class UdsFedAlertsProcessorSolvingStrategy implements UdsFedAlertsProcessorStrategy {

  private final AnalysisService analysisService;
  private final RegistrationAnalysisProperties analysisProperties;
  private final AlertRepository alertRepository;

  @Override
  public void processUdsFedAlerts(Batch batch, List<String> alertNames) {
    log.info("UDS fed alerts are being processed for solving batch with id [{}].", batch.id());
    updateAlertsStatus(batch.id(), alertNames);
    analysisService.addAlertsToAnalysis(
        batch.analysisName(),
        alertNames,
        getAlertDeadlineTime()
    );
  }

  private void updateAlertsStatus(String batchId, List<String> alertNames) {
    var statusesNotIn = EnumSet.of(AlertStatus.RECOMMENDED, AlertStatus.DELIVERED);
    log.info("Set [{}] alerts status to [{}] for batch id [{}]. Skipping [{}] alerts.",
        alertNames.size(), AlertStatus.PROCESSING, batchId, statusesNotIn);
    alertRepository.updateStatusToProcessing(batchId, alertNames, statusesNotIn);
  }

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SOLVING;
  }

  private Timestamp getAlertDeadlineTime() {
    var alertTtl = analysisProperties.alertTtl();
    var offsetDateTime = OffsetDateTime.now().plusSeconds(alertTtl.getSeconds());
    return Timestamp.newBuilder()
        .setSeconds(offsetDateTime.toEpochSecond())
        .setNanos(offsetDateTime.getNano())
        .build();
  }
}
