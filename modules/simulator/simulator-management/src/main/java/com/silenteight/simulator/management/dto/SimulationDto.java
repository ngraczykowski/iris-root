package com.silenteight.simulator.management.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class SimulationDto {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  SimulationState state;

  @NonNull
  List<String> datasetNames;

  @NonNull
  String modelName;

  @NonNull
  String progressState;

  @NonNull
  String createdBy;

  @NonNull
  OffsetDateTime createdAt;

  OffsetDateTime updatedAt;
}