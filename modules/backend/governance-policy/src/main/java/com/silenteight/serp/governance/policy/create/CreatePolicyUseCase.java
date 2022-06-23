package com.silenteight.serp.governance.policy.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;


@RequiredArgsConstructor
class CreatePolicyUseCase {

  @NonNull
  private final PolicyService policyService;

  PolicyDto activate(@NonNull CreatePolicyCommand command) {
    return policyService.createPolicy(
        command.getId(), command.getPolicyName(), command.getCreatedBy());
  }
}
