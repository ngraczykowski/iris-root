package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.management.dto.CreateSimulationRequest;

@AllArgsConstructor
public class CreateSimulationUseCase {

  @NonNull
  private final SimulationService simulationService;

  public void activate(CreateSimulationRequest request, String username) {
    simulationService.createSimulation(request, username);
  }
}
