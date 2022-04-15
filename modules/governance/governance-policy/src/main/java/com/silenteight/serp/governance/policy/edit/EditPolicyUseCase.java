package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.UpdatePolicyRequest;

@RequiredArgsConstructor
class EditPolicyUseCase {

  private final PolicyService policyService;

  void activate(@NonNull EditPolicyCommand command) {
    UpdatePolicyRequest request = UpdatePolicyRequest.of(
        command.getId(), command.getPolicyName(), command.getDescription(), command.getUpdatedBy());
    policyService.updatePolicy(request);
  }
}
