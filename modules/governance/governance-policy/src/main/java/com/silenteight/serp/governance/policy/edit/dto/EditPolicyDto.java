package com.silenteight.serp.governance.policy.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyState;

import javax.validation.constraints.Size;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_POLICY_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MIN_POLICY_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditPolicyDto {

  @Size(min = MIN_POLICY_NAME_LENGTH, max = MAX_POLICY_NAME_LENGTH)
  private String policyName;
  private String description;
  private PolicyState state;
}
