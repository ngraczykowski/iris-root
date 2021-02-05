package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@RequiredArgsConstructor
class UsePolicyUseCase {

  @NonNull
  private final PolicyService policyService;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  void activate(@NonNull UUID id, @NonNull String user) {
    policyService.usePolicy(id, user);
    eventPublisher.publishEvent(PolicyPromotedEvent.builder().policyId(id).build());
  }
}
