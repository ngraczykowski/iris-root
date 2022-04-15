package com.silenteight.simulator.management.create.dto;

import lombok.*;

import org.hibernate.validator.constraints.Length;

import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.management.domain.SimulationDomainConstants.MAX_MODEL_NAME_LENGTH;
import static com.silenteight.simulator.management.domain.SimulationDomainConstants.MIN_MODEL_NAME_LENGTH;

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
  @Length(min = MIN_MODEL_NAME_LENGTH, max = MAX_MODEL_NAME_LENGTH)
  private String model;
}
