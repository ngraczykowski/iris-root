package com.silenteight.serp.governance.policy.step.logic;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;


@RequiredArgsConstructor
class EditStepLogicUseCase {

  private final PolicyService policyService;

  private final PolicyStepsRequestQuery policyStepsRequestQuery;


  public void activate(EditStepLogicCommand command) {
    Long policyId = policyStepsRequestQuery.getPolicyIdForStep(command.getStepId());
    policyService.configureStepLogic(
        policyId,
        command.getStepId(),
        command.getFeatureLogicConfigurations(),
        command.getUser());
  }
}
