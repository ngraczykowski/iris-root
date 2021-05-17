package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.UsePolicyRequest;
import com.silenteight.serp.governance.policy.domain.events.NewPolicyInUseEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@RequiredArgsConstructor
class UsePolicyUseCase {

  @NonNull
  private final PolicyService policyService;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  void activate(@NonNull UUID id, @NonNull String user) {
    UsePolicyRequest request = UsePolicyRequest.of(id, user);
    policyService.usePolicy(request);
    NewPolicyInUseEvent event = NewPolicyInUseEvent
        .builder()
        .policyId(id)
        .correlationId(request.getCorrelationId())
        .build();
    eventPublisher.publishEvent(event);
  }
}
