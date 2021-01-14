package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.domain.PolicyService;

@RequiredArgsConstructor
class PolicyPromotedEventHandler {

  @NonNull
  private final PolicyService policyService;

  @NonNull
  private final StepPolicyFactory stepPolicyFactory;

  void handle(PolicyPromotedEvent event) {
    stepPolicyFactory.reconfigure(policyService.getPolicySteps(event.getPolicyId()));
  }
}
