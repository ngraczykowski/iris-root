package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class CloneStepRequest implements AuditableRequest {

  @NonNull
  UUID correlationId;
  @NonNull
  UUID newStepId;
  @NonNull
  UUID baseStepId;
  @NonNull
  UUID policyId;
  @NonNull
  String createdBy;

  public static final String PRE_AUDIT_TYPE = "CloneStepRequestRequested";
  public static final String POST_AUDIT_TYPE = "CloneStepRequestCreated";

  public static CloneStepRequest of(
      UUID stepId, UUID baseStepId, UUID policyId, String createdBy) {

    return CloneStepRequest.of(randomUUID(), stepId, baseStepId, policyId, createdBy);
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
        .timestamp(from(Instant.now()))
        .type(type)
        .entityId(newStepId.toString())
        .entityClass("Step")
        .entityAction("CLONE")
        .details(toString())
        .principal(createdBy)
        .build();
  }
}
