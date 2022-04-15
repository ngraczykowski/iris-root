package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.SavePolicyRequest;

import java.util.UUID;

@RequiredArgsConstructor
class SavePolicyUseCase {

  private final PolicyService policyService;

  void activate(@NonNull UUID id, @NonNull String userName) {
    policyService.savePolicy(SavePolicyRequest.of(id, userName));
  }
}
