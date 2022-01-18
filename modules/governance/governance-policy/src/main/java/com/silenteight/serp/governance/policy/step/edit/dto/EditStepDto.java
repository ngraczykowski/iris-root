package com.silenteight.serp.governance.policy.step.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.Solution;

import org.hibernate.validator.constraints.Length;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_STEP_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditStepDto {

  @Length(max = MAX_STEP_NAME_LENGTH)
  private String name;
  private String description;
  private Solution solution;
}
