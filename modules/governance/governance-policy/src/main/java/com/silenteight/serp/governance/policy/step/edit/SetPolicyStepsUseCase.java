package com.silenteight.serp.governance.policy.step.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class SetPolicyStepsUseCase {

  @NonNull
  private final PolicyService policyService;

  void activate(UUID id, List<UUID> stepsOrder, String user) {
    policyService.setStepsOrder(id, stepsOrder, user);
  }
}
