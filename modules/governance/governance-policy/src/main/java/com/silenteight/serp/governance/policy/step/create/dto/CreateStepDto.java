package com.silenteight.serp.governance.policy.step.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.domain.dto.Solution;

import java.util.UUID;
import javax.validation.constraints.Size;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_STEP_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MIN_STEP_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStepDto {

  @NonNull
  private UUID id;
  @Size(min = MIN_STEP_NAME_LENGTH, max = MAX_STEP_NAME_LENGTH)
  @NonNull
  private String name;
  private String description;
  @NonNull
  private Solution solution;
  @NonNull
  private StepType type;
}
