package com.silenteight.simulator.management.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.simulator.management.domain.SimulationState;

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
  String description;
  @NonNull
  String simulationName;
  @NonNull
  SimulationState state;
  @NonNull
  Set<String> datasets;
  @NonNull
  String model;
  @NonNull
  String analysis;
  @NonNull
  String createdBy;
  long solvedAlerts;
  @NonNull
  OffsetDateTime createdAt;
  OffsetDateTime updatedAt;
  OffsetDateTime finishedAt;
}
