package com.silenteight.simulator.management.create.dto;

import lombok.*;

import org.hibernate.validator.constraints.Length;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.Pattern;

import static com.silenteight.serp.governance.common.web.rest.RestValidationConstants.FIELD_REGEX;
import static com.silenteight.simulator.management.domain.SimulationDomainConstants.MAX_MODEL_NAME_LENGTH;
import static com.silenteight.simulator.management.domain.SimulationDomainConstants.MIN_MODEL_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSimulationRequestDto {

  @NonNull
  private UUID id;
  @NonNull
  @Pattern(regexp = FIELD_REGEX)
  private String simulationName;
  @NonNull
  @Pattern(regexp = FIELD_REGEX)
  private String description;
  @NonNull
  private Set<String> datasets;
  @NonNull
  @Length(min = MIN_MODEL_NAME_LENGTH, max = MAX_MODEL_NAME_LENGTH)
  private String model;
}
