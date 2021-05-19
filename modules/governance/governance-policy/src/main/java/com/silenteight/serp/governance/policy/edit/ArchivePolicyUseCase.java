package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ArchivePolicyRequest;

import java.util.UUID;

@RequiredArgsConstructor
class ArchivePolicyUseCase {

  @NonNull
  private final PolicyService policyService;

  void activate(UUID policyId, String userName) {
    policyService.archivePolicy(ArchivePolicyRequest.of(policyId, userName));
  }
}
