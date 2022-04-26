package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.*;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.BatchId;
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationFacade {

  private final BatchService batchService;
  private final AlertService alertService;
  private final UdsFedAlertsService udsFedAlertsService;
  private final BatchTimeoutService batchTimeoutService;

  public BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchService.register(registerBatchCommand);
  }

  public List<AlertWithMatches> getAlertsWithMatches(GetAlertsWithMatchesCommand command) {
    return alertService.getAlertsAndMatches(command.batchId());
  }

  public BatchWithAlerts getBatchWithAlerts(GetBatchWithAlertsCommand command) {
    var batchIdWithPolicy =
        batchService.findBatchIdWithPolicyByAnalysisName(command.analysisName());
    var alerts = alertService.getAlertsAndMatches(batchIdWithPolicy.id());

    return BatchWithAlerts.builder()
        .batchId(batchIdWithPolicy.id())
        .policyId(batchIdWithPolicy.policyId())
        .alerts(alerts)
        .build();
  }

  public void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    batchService.notifyBatchError(notifyBatchErrorCommand);
  }

  public void markAlertsAsRecommended(MarkAlertsAsRecommendedCommand command) {
    var batch = batchService.findBatchByAnalysisName(command.analysisName());

    log.info(
        "Batch for given analysis name [{}], has batch id [{}].",
        command.analysisName(), batch.id());

    alertService.updateStatusToRecommended(batch.id(), command.alertNames());

    if (alertService.hasNoPendingAlerts(batch) || command.isTimedOut()) {
      log.info("Completing batch, without pending alerts, with id [{}]", batch.id());
      batchService.completeSolvingBatch(batch);
    }
  }

  public List<RegistrationAlert> registerAlertsAndMatches(RegisterAlertsCommand command) {
    var batch = batchService.findPendingBatch(command.batchId());

    return alertService.registerAlertsAndMatches(command, batch.priority());
  }

  public void markAlertsAsDelivered(MarkAlertsAsDeliveredCommand command) {
    var batch = getBatch(command);
    alertService.updateStatusToDelivered(batch.id(), command.alertNames());
    if (allAlertsAreDelivered(batch, command.alertNames())) {
      batchService.markBatchAsDelivered(batch);
    }
  }

  public void processUdsFedAlerts(List<ProcessUdsFedAlertsCommand> processUdsFedAlertsCommands) {
    udsFedAlertsService.processUdsFedAlerts(processUdsFedAlertsCommands);
  }

  public void verifyBatchTimeout(VerifyBatchTimeoutCommand command) {
    batchTimeoutService.verifyBatchTimeout(command);
  }

  public void verifyBatchTimeoutForAllErroneousAlerts(VerifyBatchTimeoutCommand command) {
    batchTimeoutService.verifyBatchTimeoutForAllErroneousAlerts(command);
  }

  private boolean allAlertsAreDelivered(Batch batch, List<String> alertNames) {
    if (CollectionUtils.isEmpty(alertNames)) {
      return true;
    } else {
      return alertService.hasAllDeliveredAlerts(batch);
    }
  }

  private Batch getBatch(MarkAlertsAsDeliveredCommand command) {
    if (StringUtils.isNotEmpty(command.batchId())) {
      return batchService.findBatchById(command.batchId());
    } else if (StringUtils.isNotEmpty(command.analysisName())) {
      return batchService.findBatchByAnalysisName(command.analysisName());
    } else {
      throw new IllegalStateException("Either batchId or analysisName must be present.");
    }
  }
}
