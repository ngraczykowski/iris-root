package com.silenteight.simulator.management.create.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSimulationRequestDto {

  @NonNull
  private UUID id;
  @NonNull
  private String simulationName;
  @NonNull
  private String description;
  @NonNull
  private Set<String> datasets;
  @NonNull
  private String model;
}
