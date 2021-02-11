package com.silenteight.serp.governance.policy.step.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;

@RequiredArgsConstructor
class CreateStepUseCase {

  @NonNull
  private final PolicyService policyService;

  void activate(@NonNull CreateStepCommand command) {
    policyService.addStepToPolicy(
        command.getPolicyId(),
        command.getSolution().getFeatureVectorSolution(),
        command.getStepId(),
        command.getName(),
        command.getDescription(),
        command.getStepType(),
        command.getCreatedBy());
  }
}
