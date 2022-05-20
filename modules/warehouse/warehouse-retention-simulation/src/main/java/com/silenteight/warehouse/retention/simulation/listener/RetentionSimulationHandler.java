package com.silenteight.warehouse.retention.simulation.listener;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.warehouse.retention.simulation.RetentionSimulationUseCase;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class RetentionSimulationHandler {

  @NonNull
  private final RetentionSimulationUseCase retentionSimulationUseCase;

  public void handle(AnalysisExpired analysisExpired) {
    List<String> analysis = analysisExpired.getAnalysisList();
    log.info("Received AnalysisExpired command with {} analysis.", analysis);
    retentionSimulationUseCase.activate(analysis);
    log.debug("Processed AnalysisExpired command with {} analysis.", analysis);
  }
}
