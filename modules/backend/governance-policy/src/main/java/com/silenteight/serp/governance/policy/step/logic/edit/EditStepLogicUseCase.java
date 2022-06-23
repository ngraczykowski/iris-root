package com.silenteight.serp.governance.policy.step.logic.edit;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ConfigureStepLogicRequest;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;


@RequiredArgsConstructor
class EditStepLogicUseCase {

  private final PolicyService policyService;

  private final PolicyStepsRequestQuery policyStepsRequestQuery;

  public void activate(EditStepLogicCommand command) {
    Long policyId = policyStepsRequestQuery.getPolicyIdForStep(command.getStepId());
    ConfigureStepLogicRequest request = ConfigureStepLogicRequest.of(
        policyId,
        command.getStepId(),
        command.getFeatureLogicConfigurations(),
        command.getUser());
    policyService.configureStepLogic(request);
  }
}
