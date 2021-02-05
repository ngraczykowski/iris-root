package com.silenteight.serp.governance.policy.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditPolicyDto {

  private String name;
  private String description;
  private PolicyState state;
}
