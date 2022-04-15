package com.silenteight.serp.governance.policy.step.clone;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.CloneStepRequest;

import java.util.UUID;

@RequiredArgsConstructor
class CloneStepUseCase {

  @NonNull
  private final PolicyService policyService;

  UUID activate(@NonNull CloneStepCommand command) {
    CloneStepRequest request =
        CloneStepRequest.of(
            command.getNewStepId(),
            command.getBaseStepId(),
            command.getPolicyId(),
            command.getCreatedBy());

    return policyService.cloneStep(request);
  }
}
