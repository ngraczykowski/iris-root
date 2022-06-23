package com.silenteight.serp.governance.policy.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.DeletePolicyRequest;

@RequiredArgsConstructor
class DeletePolicyUseCase {

  @NonNull
  private final PolicyService policyService;

  void activate(@NonNull DeletePolicyCommand command) {
    policyService.deletePolicy(DeletePolicyRequest.of(command.getId(), command.getDeletedBy()));
  }
}
