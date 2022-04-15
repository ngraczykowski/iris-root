package com.silenteight.simulator.management.timeout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.management.domain.dto.SimulationDto;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class AnalysisTimeoutValidator extends BaseSimulationTimeoutValidator {

  @NonNull
  private final AnalysisService analysisService;

  @NonNull
  private final SimulationService simulationService;

  private static final String TIMEOUT_MESSAGE = "Timeout on analysis validation for simulation";
  private static final String NO_TIMEOUT_MESSAGE =
      "No timeout on analysis validation for simulation";

  @Override
  public boolean valid(SimulationDto simulationDto) {
    log.info("Validating simulation on analysis level, simulationdId={}", simulationDto.getId());
    boolean result = timeoutOnSolvingProgress(simulationDto);
    doLog(result);
    return result;
  }

  private boolean timeoutOnSolvingProgress(SimulationDto simulationDto) {

    String analysisName = simulationDto.getAnalysis();
    UUID simulationId = simulationDto.getId();
    Analysis analysis = analysisService.getAnalysis(analysisName);

    long alreadySolvedAlerts = simulationDto.getSolvedAlerts();
    long totalAlerts = analysis.getAlertCount();
    long pendingAlerts = analysis.getPendingAlerts();
    long solvedAlerts = totalAlerts - pendingAlerts;

    if (pendingAlerts == 0) {
      log.info("All alerts in analysis={} are resolved.", analysis.getName());
      simulationService.updateNumberOfSolvedAlertsInSimulation(solvedAlerts, simulationId);
      return false;
    }

    if (alreadySolvedAlerts == solvedAlerts) {
      log.info(
          "Number of resolved alerts in analysis={} has not been changed since last check.",
          analysisName);

      return true;
    }
    simulationService.updateNumberOfSolvedAlertsInSimulation(solvedAlerts, simulationId);
    return false;
  }

  @Override
  protected String getTimeoutMessage() {
    return TIMEOUT_MESSAGE;
  }

  @Override
  protected String getNoTimeoutMessage() {
    return NO_TIMEOUT_MESSAGE;
  }
}
