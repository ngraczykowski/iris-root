package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;

import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.time.Instant.*;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class PromotePolicyRequest {

  @NonNull
  UUID correlationId;
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
    logger.accept(getAuditDataDto("Changing state to TO_BE_USED"));
  }

  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto("Changed state to TO_BE_USED"));
  }

  private AuditDataDto getAuditDataDto(String type) {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(from(now()))
        .type(type)
        .entityId(policyId.toString())
        .entityClass("Policy")
        .entityAction("UPDATE")
        .details(toString())
        .principal(activatedBy)
        .build();
  }
}
