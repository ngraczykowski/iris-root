package com.silenteight.serp.governance.policy.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyState;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.silenteight.serp.governance.common.web.rest.RestValidationConstants.FIELD_REGEX;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_POLICY_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MIN_POLICY_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditPolicyDto {

  @Size(min = MIN_POLICY_NAME_LENGTH, max = MAX_POLICY_NAME_LENGTH)
  @Pattern(regexp = FIELD_REGEX)
  private String policyName;
  @Pattern(regexp = FIELD_REGEX)
  private String description;
  private PolicyState state;
}
