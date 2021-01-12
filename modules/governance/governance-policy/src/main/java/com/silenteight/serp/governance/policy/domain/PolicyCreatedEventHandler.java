package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.context.ApplicationEventPublisher;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
class PolicyCreatedEventHandler {

  @NonNull
  private final AuditingLogger auditingLogger;

  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  void handle(PolicyCreatedEvent event) {
    logPolicyCreated(event.getPolicyId(), event.getCorrelationId());
    eventPublisher.publishEvent(new PolicyPromotedEvent(event.getPolicyId()));
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
        .details(policyId.toString())
        .build();
    auditingLogger.log(auditDataDto);
  }
}
