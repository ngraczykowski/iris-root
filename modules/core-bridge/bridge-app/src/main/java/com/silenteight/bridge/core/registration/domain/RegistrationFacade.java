package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.model.BatchId;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationFacade {

  private final BatchService batchService;
  private final AlertAnalysisService alertAnalysisService;

  public BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchService.register(registerBatchCommand);
  }

  public void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    batchService.notifyBatchError(notifyBatchErrorCommand);
  }

  public void addAlertsToAnalysis(List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    alertAnalysisService.addAlertsToAnalysis(addAlertToAnalysisCommands);
  }
}
