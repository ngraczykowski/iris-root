package com.silenteight.simulator.management.domain.exception;

import lombok.NonNull;

import com.silenteight.simulator.management.domain.SimulationState;

import java.util.Arrays;

import static java.lang.String.format;

public class SimulationNotInProperStateException extends RuntimeException {

  private static final long serialVersionUID = -1637272806505933726L;

  public SimulationNotInProperStateException(@NonNull SimulationState... requiredStates) {
    super(format("Simulation should be in state: %s.", Arrays.asList(requiredStates)));
  }
}
