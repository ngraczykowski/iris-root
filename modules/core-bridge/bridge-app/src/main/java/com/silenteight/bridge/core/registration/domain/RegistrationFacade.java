package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.BatchId;

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

  public void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    batchService.notifyBatchError(notifyBatchErrorCommand);
  }

  public void markAlertsAsRecommended(
      MarkAlertsAsRecommendedCommand markAlertsAsRecommendedCommand) {
    var batchId = batchService.findBatch(markAlertsAsRecommendedCommand.analysisName());

    log.info(
        "Batch for given analysis name {}, has batch id: {}.",
        markAlertsAsRecommendedCommand.analysisName(), batchId.id());

    alertService.updateStatusToRecommended(
        batchId.id(), markAlertsAsRecommendedCommand.alertNames());

    if (alertService.hasNoPendingAlerts(batchId.id())) {
      batchService.completeBatch(
          new CompleteBatchCommand(batchId.id(), markAlertsAsRecommendedCommand.alertNames()));
    }
  }

  public void registerAlertsAndMatches(RegisterAlertsCommand registerAlertsCommand) {
    alertService.registerAlertsAndMatches(registerAlertsCommand);
  }

  public void addAlertsToAnalysis(List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    alertAnalysisService.addAlertsToAnalysis(addAlertToAnalysisCommands);
  }
}
