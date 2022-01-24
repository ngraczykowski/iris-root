package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.GetBatchWithAlertsCommand;
import com.silenteight.bridge.core.registration.domain.model.BatchId;
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationFacade {

  private final BatchService batchService;
  private final AlertService alertService;
  private final AlertAnalysisService alertAnalysisService;

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
    var batchId = batchService.findBatchId(command.analysisName()).id();

    log.info(
        "Batch for given analysis name {}, has batch id: {}.",
        command.analysisName(), batchId);

    alertService.updateStatusToRecommended(batchId, command.alertNames());

    if (alertService.hasNoPendingAlerts(batchId)) {
      batchService.completeBatch(new CompleteBatchCommand(batchId, command.alertNames()));
    }
  }

  public void markBatchAsDelivered(MarkBatchAsDeliveredCommand markBatchAsDeliveredCommand) {
    batchService.markBatchAsDelivered(markBatchAsDeliveredCommand.batchId());
  }

  public void registerAlertsAndMatches(RegisterAlertsCommand registerAlertsCommand) {
    alertService.registerAlertsAndMatches(registerAlertsCommand);
  }

  public void addAlertsToAnalysis(List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    alertAnalysisService.addAlertsToAnalysis(addAlertToAnalysisCommands);
  }
}
