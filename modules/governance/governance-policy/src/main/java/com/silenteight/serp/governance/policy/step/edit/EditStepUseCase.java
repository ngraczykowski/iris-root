package com.silenteight.serp.governance.policy.step.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.UpdateStepRequest;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

@RequiredArgsConstructor
class EditStepUseCase {

  @NonNull
  private final PolicyService policyService;
  @NonNull
  private final PolicyStepsRequestQuery policyStepsRequestQuery;

  void activate(@NonNull EditStepCommand command) {
    Long policyId = policyStepsRequestQuery.getPolicyIdForStep(command.getId());
    policyService.updateStep(
        UpdateStepRequest.of(
            policyId,
            command.getId(),
            command.getName(),
            command.getDescription(),
            command.getFeatureVectorSolution(),
            command.getUpdatedBy()));
  }
}
