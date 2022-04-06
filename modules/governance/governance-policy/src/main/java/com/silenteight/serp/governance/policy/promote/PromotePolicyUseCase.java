package com.silenteight.serp.governance.policy.promote;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.PromotePolicyRequest;

import static com.silenteight.serp.governance.policy.common.PolicyResource.fromResourceName;

@Slf4j
@RequiredArgsConstructor
public class PromotePolicyUseCase {

  private final PolicyService policyService;

  public void activate(PromotePolicyCommand promotePolicyCommand) {
    log.info("PromotePolicyCommand request received, command={}.", promotePolicyCommand);

    PromotePolicyRequest promotePolicyRequest = PromotePolicyRequest.of(
        promotePolicyCommand.getCorrelationId(),
        fromResourceName(promotePolicyCommand.getPolicyName()),
        promotePolicyCommand.getPromotedBy());
    policyService.promotePolicy(promotePolicyRequest);

    log.debug(
        "PromotePolicyCommand request for policyId={} processed.",
        promotePolicyRequest.getPolicyId());
  }
}
