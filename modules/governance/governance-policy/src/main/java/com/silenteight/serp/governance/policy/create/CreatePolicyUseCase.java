package com.silenteight.serp.governance.policy.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import java.util.UUID;

@RequiredArgsConstructor
class CreatePolicyUseCase {

  @NonNull
  private final PolicyService policyService;

  UUID activate(@NonNull CreatePolicyCommand command) {
    return policyService.addPolicy(
        command.getId(), command.getName(), command.getCreatedBy());
  }
}
