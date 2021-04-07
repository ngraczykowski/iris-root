package com.silenteight.simulator.management;

import java.util.UUID;

class NonUniqueSimulationException extends RuntimeException {

  private static final long serialVersionUID = 2241514797096993648L;

  NonUniqueSimulationException(UUID simulationId) {
    super(String.format("There is a conflict with an existing simulation. "
        + "Creating simulation failed: simulationId=%s.", simulationId));
  }
}
