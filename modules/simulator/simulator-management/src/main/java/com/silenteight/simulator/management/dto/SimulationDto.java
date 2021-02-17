package com.silenteight.simulator.management.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class SimulationDto {

  @NonNull
  java.util.UUID id;

  @NonNull
  String name;

  @NonNull
  UUID datasetId;

  @NonNull
  UUID policyId;

  @NonNull
  SimulationState status;

  @NonNull
  String createdBy;

  @NonNull
  OffsetDateTime createdAt;

  OffsetDateTime updatedAt;
}