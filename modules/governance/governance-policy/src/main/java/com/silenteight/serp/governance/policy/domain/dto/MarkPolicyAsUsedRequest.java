package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class MarkPolicyAsUsedRequest {

  UUID correlationId = randomUUID();
  @NonNull
  UUID policyId;
  @NonNull
  String activatedBy;

  public UUID getPolicyId() {
    return policyId;
  }

  public String getActivatedBy() {
    return activatedBy;
  }

  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto("Changing state to IN_USE"));
  }

  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto("Changed state to IN_USE"));
  }

  private AuditDataDto getAuditDataDto(String type) {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type(type)
        .entityId(policyId.toString())
        .entityClass("Policy")
        .entityAction("UPDATE")
        .details(toString())
        .principal(activatedBy)
        .build();
  }
}
