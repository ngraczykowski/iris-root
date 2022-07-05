/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.domain.State;

import java.util.Map;

import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;

@Slf4j
@RequiredArgsConstructor
class SimulationFinishChecker {

  private final SimulationService simulationService;
  private final AnalysisService analysisService;

  @Timed(
      percentiles = {0.5, 0.95, 0.99},
      histogram = true)
  public void check(String analysisName, Map<State, Long> statuses) {
    var indexedAlerts = statuses.getOrDefault(ACKED, 0L);
    var sentAlerts = statuses.getOrDefault(SENT, 0L);
    log.debug("Checking analyseName={} -> statuses={}", analysisName, statuses);
    var alertsCount = getAlertsCountByAnalysisName(analysisName);
    log.debug("Checking analyseName={} -> alertCount={}", analysisName, alertsCount);

    if (shouldFinishSimulation(indexedAlerts, sentAlerts, alertsCount)) {
      log.debug("All indexed alerts for analysisName={} are 'ACKED'", analysisName);
      simulationService.finish(analysisName);
      log.info("Simulation with analysisName={} is finished", analysisName);
    }
  }

  private long getAlertsCountByAnalysisName(String analysisName) {
    return analysisService.getAnalysis(analysisName).getAlertCount();
  }

  private static boolean shouldFinishSimulation(
      long indexedAlerts, long sentAlerts, long alertsCount) {
    return sentAlerts == 0L && alertsCount == indexedAlerts;
  }
}
