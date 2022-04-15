package com.silenteight.serp.governance.policy.step.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.CreateStepRequest;

@RequiredArgsConstructor
class CreateStepUseCase {

  @NonNull
  private final PolicyService policyService;

  void activate(@NonNull CreateStepCommand command) {
    CreateStepRequest request = CreateStepRequest.of(
        command.getPolicyId(),
        command.getSolution().getFeatureVectorSolution(),
        command.getStepId(),
        command.getName(),
        command.getDescription(),
        command.getStepType(),
        command.getCreatedBy());
    policyService.addStepToPolicy(request);
  }
}
