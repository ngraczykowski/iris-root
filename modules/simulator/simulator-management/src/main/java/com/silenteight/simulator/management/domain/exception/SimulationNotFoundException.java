package com.silenteight.simulator.management.domain.exception;

import lombok.NonNull;

import java.util.UUID;

public class SimulationNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4500847324520654389L;

  public SimulationNotFoundException(@NonNull UUID simulationId) {
    super(String.format("Simulation with simulationId=%s not found.", simulationId.toString()));
  }
}
