package com.silenteight.serp.governance.policy.step.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.Solution;

import javax.validation.constraints.Size;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_STEP_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MIN_STEP_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditStepDto {

  @Size(min = MIN_STEP_NAME_LENGTH, max = MAX_STEP_NAME_LENGTH)
  private String name;
  private String description;
  private Solution solution;
}
