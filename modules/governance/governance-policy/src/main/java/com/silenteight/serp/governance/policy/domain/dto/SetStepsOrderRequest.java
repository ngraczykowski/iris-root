package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class SetStepsOrderRequest implements AuditableRequest {

  UUID correlationId = UUID.randomUUID();
  @NonNull
  UUID policyId;
  @NonNull
  List<UUID> stepsOrder;
  @NonNull
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
        .type(this.getClass().getSimpleName())
        .entityId(policyId.toString())
        .entityClass("Step")
        .entityAction("UPDATE")
        .details(this.toString())
        .principal(updatedBy)
        .build();
  }
}
