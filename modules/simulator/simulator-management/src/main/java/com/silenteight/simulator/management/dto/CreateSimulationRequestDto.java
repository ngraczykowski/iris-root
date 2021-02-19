package com.silenteight.simulator.management.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateSimulationRequestDto {

  @NonNull
  UUID id;

  @NonNull
  String name;

  String description;

  @NonNull
  UUID datasetId;

  @NonNull
  UUID policyId;
}