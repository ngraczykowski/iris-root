package com.silenteight.simulator.management.timeout;

import com.silenteight.simulator.management.domain.dto.SimulationDto;

public interface SimulationTimeoutValidator {

  boolean valid(SimulationDto simulationDto);
}
