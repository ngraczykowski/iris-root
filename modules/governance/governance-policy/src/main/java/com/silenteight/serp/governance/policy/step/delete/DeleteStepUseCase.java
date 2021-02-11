package com.silenteight.serp.governance.policy.step.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

@RequiredArgsConstructor
class DeleteStepUseCase {

  @NonNull
  private final PolicyService policyService;
  @NonNull
  private final PolicyStepsRequestQuery policyStepsRequestQuery;

  void activate(@NonNull DeleteStepCommand command) {
    Long policyId = policyStepsRequestQuery.getPolicyIdForStep(command.getId());
    policyService.deleteStep(policyId, command.getId(), command.getUpdatedBy());
  }
}
