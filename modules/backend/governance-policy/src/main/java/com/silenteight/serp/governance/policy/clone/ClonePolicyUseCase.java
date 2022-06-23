package com.silenteight.serp.governance.policy.clone;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ClonePolicyRequest;

import java.util.UUID;

@RequiredArgsConstructor
class ClonePolicyUseCase {

  @NonNull
  private final PolicyService policyService;

  UUID activate(@NonNull ClonePolicyCommand command) {
    return policyService.clonePolicy(ClonePolicyRequest.of(
        command.getId(),
        command.getBasePolicyId(),
        command.getCreatedBy())
    );
  }
}
