package com.silenteight.serp.governance.policy.step.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.Solution;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditStepDto {

  private String name;
  private String description;
  private Solution solution;
}
