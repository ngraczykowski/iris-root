package com.silenteight.simulator.management.progress;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;

import java.util.UUID;

@RequiredArgsConstructor
class SimulationProgressService {

  @NonNull
  private final AnalysisService analysisService;

  @NonNull
  private final IndexedAlertProvider indexedAlertProvider;

  @NonNull
  private final SimulationService simulationService;

  SimulationProgressDto getProgress(UUID simulationId) {
    String analysisName = simulationService.getAnalysisNameBySimulationId(simulationId);

    Analysis analysis = analysisService.getAnalysis(analysisName);

    long alertsCount = analysis.getAlertCount();
    long pendingAlerts = analysis.getPendingAlerts();
    long solvedAlerts = calculatedSolvedAlert(alertsCount, pendingAlerts);
    long indexedAlerts = indexedAlertProvider.getAllIndexedAlertsCount(analysisName);

    return SimulationProgressDto.builder()
        .allAlerts(alertsCount)
        .solvedAlerts(solvedAlerts)
        .indexedAlerts(indexedAlerts)
        .build();
  }

  private static long calculatedSolvedAlert(long alertsCount, long pendingAlerts) {
    return alertsCount - pendingAlerts;
  }
}
