package com.silenteight.simulator.management.domain.exception;

import java.util.UUID;

import static java.lang.String.format;

public class SimulationNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4500847324520654389L;

  public SimulationNotFoundException(UUID simulationId) {
    super(format("Simulation with simulationId=%s not found.", simulationId));
  }

  public SimulationNotFoundException(String analysisName) {
    super(format("Simulation with analysisName=%s not found.", analysisName));
  }
}
