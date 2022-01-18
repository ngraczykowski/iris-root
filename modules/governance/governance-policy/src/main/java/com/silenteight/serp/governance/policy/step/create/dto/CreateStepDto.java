package com.silenteight.serp.governance.policy.step.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.domain.dto.Solution;

import org.hibernate.validator.constraints.Length;

import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_STEP_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStepDto {

  @NonNull
  private UUID id;
  @Length(max = MAX_STEP_NAME_LENGTH)
  @NonNull
  private String name;
  private String description;
  @NonNull
  private Solution solution;
  @NonNull
  private StepType type;
}
