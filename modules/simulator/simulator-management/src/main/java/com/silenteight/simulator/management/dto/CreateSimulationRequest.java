package com.silenteight.simulator.management.dto;

import lombok.*;

import java.util.UUID;

@Value
@Builder
public class CreateSimulationRequest {

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