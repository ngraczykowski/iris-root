package com.silenteight.simulator.management.domain;

import java.util.UUID;

public class NonUniqueSimulationException extends RuntimeException {

  private static final long serialVersionUID = 2241514797096993648L;

  public NonUniqueSimulationException(UUID simulationId) {
    super(String.format("There is a conflict with an existing simulation. "
        + "Creating simulation failed: simulationId=%s.", simulationId));
  }
}
