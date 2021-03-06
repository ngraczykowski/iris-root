package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class UpdatePolicyRequest {

  UUID correlationId = UUID.randomUUID();
  @NonNull
  UUID id;
  @Nullable
  String policyName;
  @Nullable
  String description;
  @Nullable
  String updatedBy;

  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto());
  }

  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto());
  }

  private AuditDataDto getAuditDataDto() {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type(getClass().getSimpleName())
        .entityId(id.toString())
        .entityClass("Policy")
        .entityAction("UPDATE")
        .details(toString())
        .principal(updatedBy)
        .build();
  }
}
