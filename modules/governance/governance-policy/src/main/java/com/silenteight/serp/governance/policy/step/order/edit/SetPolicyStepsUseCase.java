package com.silenteight.serp.governance.policy.step.order.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;

@RequiredArgsConstructor
class SetPolicyStepsUseCase {

  @NonNull
  private final PolicyService policyService;

  public void activate(SetPolicyStepsOrderCommand command) {
    policyService.setStepsOrder(
        command.getPolicyId(), command.getStepsOrder(), command.getUpdatedBy());
  }
}
