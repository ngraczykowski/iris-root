package com.silenteight.simulator.management.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimulationDomainConstants {

  public static final int MIN_MODEL_NAME_LENGTH = 3;
  public static final int MAX_MODEL_NAME_LENGTH = 64;
}
