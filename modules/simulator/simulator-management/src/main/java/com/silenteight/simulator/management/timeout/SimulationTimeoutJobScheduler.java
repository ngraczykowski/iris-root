package com.silenteight.simulator.management.timeout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
class SimulationTimeoutJobScheduler {

  @NonNull
  SimulationTimeoutService timeoutService;

  @Scheduled(cron = "${simulator.simulation.timeout.cron}")
  void handleUpdate() {
    log.info("Simulation timeout progress process started.");
    timeoutService.timeoutSimulations();
  }
}
