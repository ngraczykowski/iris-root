package com.silenteight.warehouse.retention.simulation;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;

@Slf4j
@RequiredArgsConstructor
class RetentionSimulationHandler {

  @NonNull
  private final RetentionSimulationUseCase retentionSimulationUseCase;

  public void handle(AnalysisExpired analysisExpired) {
    log.info(
        "Received AnalysisExpired command with {} analysis.",
        analysisExpired.getAnalysisList());

    retentionSimulationUseCase.activate(analysisExpired);
    log.debug(
        "Processed AnalysisExpired command with {} analysis.",
        analysisExpired.getAnalysisList());
  }
}
