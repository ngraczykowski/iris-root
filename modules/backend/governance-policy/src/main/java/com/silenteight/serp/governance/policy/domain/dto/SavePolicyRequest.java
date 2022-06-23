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
public class SavePolicyRequest {

  public static final String PRE_AUDIT_TYPE = "SavePolicyRequestRequested";
  public static final String POST_AUDIT_TYPE = "PolicySaved";

  UUID correlationId = UUID.randomUUID();
  @NonNull
  UUID policyId;
  @NonNull
  String savedBy;

  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(PRE_AUDIT_TYPE));
  }

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
        .entityAction("UPDATE")
        .details(toString())
        .principal(savedBy)
        .build();
  }
}
