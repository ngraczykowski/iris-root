package com.silenteight.simulator.management.details;

import lombok.NonNull;

import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;

import java.util.UUID;

public interface SimulationDetailsQuery {

  SimulationDetailsDto get(@NonNull UUID simulationId);

  SimulationDetailsDto get(@NonNull String analysisName);
}
