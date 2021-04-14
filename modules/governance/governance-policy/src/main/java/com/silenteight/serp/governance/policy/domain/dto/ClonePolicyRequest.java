package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class ClonePolicyRequest implements AuditableRequest {

  @NonNull
  UUID correlationId;
  @NonNull
  UUID policyId;
  @NonNull
  UUID basePolicyId;
  @NonNull
  String createdBy;

  public static final String PRE_AUDIT_TYPE = "ClonePolicyRequestRequested";
  public static final String POST_AUDIT_TYPE = "ClonePolicyRequestCreated";

  public static ClonePolicyRequest of(
      UUID policyId, UUID basePolicyId, String createdBy) {
    return ClonePolicyRequest.of(randomUUID(), policyId, basePolicyId, createdBy);
  }

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(PRE_AUDIT_TYPE));
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(POST_AUDIT_TYPE));
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
        .entityAction("CLONE")
        .details(this.toString())
        .principal(createdBy)
        .build();
  }
}
