package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.AlertsAddedToAnalysis.Status;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.infrastructure.application.RegistrationAnalysisProperties;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
class UdsFedAlertsProcessorSolvingStrategy implements UdsFedAlertsProcessorStrategy {

  private static final String ALERT_ERROR_DESCRIPTION = "Failed to add alerts to analysis.";
  private final AnalysisService analysisService;
  private final RegistrationAnalysisProperties analysisProperties;
  private final AlertRepository alertRepository;

  @Override
  public void processUdsFedAlerts(Batch batch, List<String> alertNames) {
    log.info("UDS fed alerts are being processed for solving batch with id [{}].", batch.id());
    var alertsAddedToAnalysis =
        analysisService.addAlertsToAnalysis(
            batch.analysisName(), alertNames, getAlertDeadlineTime());

    if (Status.SUCCESS == alertsAddedToAnalysis.status()) {
      updateAlertsStatusToProcessing(batch.id(), alertNames);
    } else {
      updateAlertsStatusToError(batch.id(), alertNames);
    }
  }

  private void updateAlertsStatusToProcessing(String batchId, List<String> alertNames) {
    var statusesNotIn = EnumSet.of(AlertStatus.RECOMMENDED, AlertStatus.DELIVERED);
    log.info("Set [{}] alerts status to [{}] for batch id [{}]. Skipping [{}] alerts.",
        alertNames.size(), AlertStatus.PROCESSING, batchId, statusesNotIn);
    alertRepository.updateStatusToProcessing(batchId, alertNames, statusesNotIn);
  }

  private void updateAlertsStatusToError(String batchId, List<String> alertNames) {
    var statusesNotIn = EnumSet.of(AlertStatus.RECOMMENDED, AlertStatus.DELIVERED);
    log.info("Set [{}] alerts status to [{}] for batch id [{}]. Skipping [{}] alerts.",
        alertNames.size(), AlertStatus.ERROR, batchId, statusesNotIn);
    var errorDescriptionsWithAlertNames = createErrorDescriptionsWithAlertNames(alertNames);
    alertRepository.updateStatusToError(batchId, errorDescriptionsWithAlertNames, statusesNotIn);
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

  private Map<String, Set<String>> createErrorDescriptionsWithAlertNames(
      List<String> alertNames) {
    return Map.of(ALERT_ERROR_DESCRIPTION, new HashSet<>(alertNames));
  }
}
