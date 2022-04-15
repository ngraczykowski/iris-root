package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ArchivePolicyRequest;
import com.silenteight.serp.governance.policy.domain.events.PolicyArchivedEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class ArchivePolicyUseCase {

  @NonNull
  private final PolicyService policyService;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  void activate(UUID policyId, String userName) {
    log.info("Archiving policy: policyId={}, userName={}", policyId, userName);

    ArchivePolicyRequest request = ArchivePolicyRequest.of(policyId, userName);
    policyService.archivePolicy(request);

    log.debug("Policy archived: policyId={}, userName={}", policyId, userName);

    PolicyArchivedEvent event = PolicyArchivedEvent
        .builder()
        .policyId(policyId)
        .correlationId(request.getCorrelationId())
        .build();
    eventPublisher.publishEvent(event);
  }
}
