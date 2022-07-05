/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
class SimulationFinishScheduler {
  private final SimulationRunningQuery simulationRunningQuery;
  private final SimulationFinishChecker simulationFinishChecker;

  @Scheduled(fixedRateString = "${simulator.simulation.check-simulation.fixed-rate:PT10S}")
  public void scheduleSimulation() {
    log.debug("Check finished simulations");
    simulationRunningQuery.indexedAlertStatusesInAnalysis().forEach(simulationFinishChecker::check);
  }
}
