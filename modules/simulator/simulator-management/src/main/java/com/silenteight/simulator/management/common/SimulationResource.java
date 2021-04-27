package com.silenteight.simulator.management.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimulationResource {

  private static final String RESOURCE_NAME_PREFIX = "simulations/";

  public static String toResourceName(UUID id) {
    return RESOURCE_NAME_PREFIX + id.toString();
  }
}
