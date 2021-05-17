package com.silenteight.serp.governance.policy.promote;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.PromotePolicyRequest;

import static com.silenteight.serp.governance.policy.common.PolicyResource.fromResourceName;

@RequiredArgsConstructor
public class PromotePolicyUseCase {

  private final PolicyService policyService;

  public void activate(PromotePolicyCommand promotePolicyCommand) {
    PromotePolicyRequest promotePolicyRequest = PromotePolicyRequest.of(
        promotePolicyCommand.getCorrelationId(),
        fromResourceName(promotePolicyCommand.getPolicyName()),
        promotePolicyCommand.getPromotedBy());
    policyService.promotePolicy(promotePolicyRequest);
  }
}
