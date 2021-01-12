package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

@RequiredArgsConstructor
class PolicyPromotedEventHandler {

  @NonNull
  private final PolicyService policyService;

  @NonNull
  private final StepPolicyFactory stepPolicyFactory;

  void handle(PolicyPromotedEvent event) {
    PolicyDto policy = policyService.getPolicy(event.getPolicyId());
    stepPolicyFactory.reconfigure(policy.getSteps());
  }
}
