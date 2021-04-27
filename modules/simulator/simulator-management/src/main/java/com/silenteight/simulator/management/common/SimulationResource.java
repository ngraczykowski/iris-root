package com.silenteight.simulator.management.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimulationResource {

  private static final String SIMULATION_RESOURCE_NAME = "simulations/";

  public static String toResourceName(UUID datasetId) {
    return SIMULATION_RESOURCE_NAME + datasetId.toString();
  }
}
