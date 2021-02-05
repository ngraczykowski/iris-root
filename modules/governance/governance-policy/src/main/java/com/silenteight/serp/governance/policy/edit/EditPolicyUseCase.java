package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;

@RequiredArgsConstructor
class EditPolicyUseCase {

  private final PolicyService policyService;

  void activate(@NonNull EditPolicyCommand command) {
    policyService.updatePolicy(
        command.getId(), command.getName(), command.getDescription(), command.getUpdatedBy());
  }
}
