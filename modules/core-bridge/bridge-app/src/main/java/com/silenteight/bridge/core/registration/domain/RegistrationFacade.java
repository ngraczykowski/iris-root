package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.*;
import com.silenteight.bridge.core.registration.domain.model.BatchId;
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationFacade {

  private final BatchService batchService;
  private final AlertService alertService;
  private final AlertAnalysisService alertAnalysisService;
  private final BatchTimeoutService batchTimeoutService;

  public BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchService.register(registerBatchCommand);
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
        "Batch for given analysis name {}, has batch id: {}.",
        command.analysisName(), batch.id());

    alertService.updateStatusToRecommended(batch.id(), command.alertNames());

    if (alertService.hasNoPendingAlerts(batch.id())) {
      batchService.completeBatch(new CompleteBatchCommand(batch));
    }
  }

  public List<RegistrationAlert> registerAlertsAndMatches(RegisterAlertsCommand command) {
    var batchPriority = batchService.findBatchPriority(command.batchId()).priority();
    return alertService.registerAlertsAndMatches(command, batchPriority);
  }

  public void markBatchAsDelivered(MarkBatchAsDeliveredCommand markBatchAsDeliveredCommand) {
    batchService.markBatchAsDelivered(markBatchAsDeliveredCommand.batchId());
  }

  public void addAlertsToAnalysis(List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    alertAnalysisService.addAlertsToAnalysis(addAlertToAnalysisCommands);
  }

  public void verifyBatchTimeout(VerifyBatchTimeoutCommand command) {
    batchTimeoutService.verifyBatchTimeout(command);
  }

  public void verifyBatchTimeoutForAllErroneousAlerts(VerifyBatchTimeoutCommand command) {
    batchTimeoutService.verifyBatchTimeoutForAllErroneousAlerts(command);
  }
}
