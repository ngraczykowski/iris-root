package com.silenteight.simulator.management.domain;

import java.util.UUID;

import static java.lang.String.format;

public class NonUniqueSimulationException extends RuntimeException {

  private static final long serialVersionUID = 2241514797096993648L;

  public NonUniqueSimulationException(UUID simulationId, Throwable cause) {
    super(format("There is a conflict with an existing simulation. "
            + "Creating simulation failed: simulationId=%s.", simulationId), cause);
  }
}
