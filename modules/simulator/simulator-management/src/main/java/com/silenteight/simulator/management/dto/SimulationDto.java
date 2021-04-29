package com.silenteight.simulator.management.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class SimulationDto {

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String simulationName;
  @NonNull
  SimulationState state;
  @NonNull
  Set<String> datasets;
  @NonNull
  String model;
  @NonNull
  String progressState;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;
  OffsetDateTime updatedAt;
}
