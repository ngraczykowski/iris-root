package com.silenteight.simulator.management.progress;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class SimulationProgressUseCase {

  @NonNull
  private final SimulationProgressService simulationProgressService;

  SimulationProgressDto activate(UUID simulationId) {
    return simulationProgressService.getProgress(simulationId);
  }
}
