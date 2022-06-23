package com.silenteight.serp.governance.policy.step.order.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.SetStepsOrderRequest;

@RequiredArgsConstructor
class SetPolicyStepsUseCase {

  @NonNull
  private final PolicyService policyService;

  public void activate(SetPolicyStepsOrderCommand command) {
    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(command.getPolicyId(), command.getStepsOrder(), command.getUpdatedBy());
    policyService.setStepsOrder(request);
  }
}
