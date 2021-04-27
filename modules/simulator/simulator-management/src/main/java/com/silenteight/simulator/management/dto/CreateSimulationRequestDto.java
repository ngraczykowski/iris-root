package com.silenteight.simulator.management.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class CreateSimulationRequestDto {

  @NonNull
  UUID id;

  @NonNull
  String simulationName;

  String description;

  @NonNull
  Set<String> datasetNames;

  @NonNull
  String modelName;
}
