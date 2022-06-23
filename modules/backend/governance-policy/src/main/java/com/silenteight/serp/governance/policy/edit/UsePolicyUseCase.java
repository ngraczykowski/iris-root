package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.UsePolicyRequest;
import com.silenteight.serp.governance.policy.domain.events.NewPolicyInUseEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class UsePolicyUseCase {

  @NonNull
  private final PolicyService policyService;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  void activate(@NonNull UUID id, @NonNull String userName) {
    log.info("Setting policy as 'IN_USE', policyId={}, userName={}", id, userName);

    UsePolicyRequest request = UsePolicyRequest.of(id, userName);
    policyService.usePolicy(request);

    log.debug("Policy set as 'IN_USE, policyId={}", id);
    NewPolicyInUseEvent event = NewPolicyInUseEvent
        .builder()
        .policyId(id)
        .correlationId(request.getCorrelationId())
        .build();
    eventPublisher.publishEvent(event);
  }
}
