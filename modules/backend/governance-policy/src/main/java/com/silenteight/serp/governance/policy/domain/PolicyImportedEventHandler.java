package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.policy.domain.events.NewPolicyInUseEvent;
import com.silenteight.serp.governance.policy.domain.events.PolicyImportedEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
class PolicyImportedEventHandler {

  @NonNull
  private final AuditingLogger auditingLogger;

  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void handle(PolicyImportedEvent event) {
    logPolicyCreated(event.getPolicyId(), event.getCorrelationId());
    NewPolicyInUseEvent promotedEvent = NewPolicyInUseEvent.builder()
        .policyId(event.getPolicyId())
        .correlationId(event.getCorrelationId())
        .build();
    eventPublisher.publishEvent(promotedEvent);
  }

  private void logPolicyCreated(UUID policyId, UUID correlationId) {
    AuditDataDto auditDataDto = AuditDataDto.builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type("PolicyCreated")
        .entityId(policyId.toString())
        .entityClass("Policy")
        .entityAction("CREATE")
        .build();
    auditingLogger.log(auditDataDto);
  }
}
