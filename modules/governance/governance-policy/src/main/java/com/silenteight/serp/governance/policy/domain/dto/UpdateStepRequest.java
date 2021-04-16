package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class UpdateStepRequest implements AuditableRequest {

  UUID correlationId = UUID.randomUUID();

  long id;
  @NonNull
  UUID stepId;
  @Nullable
  String name;
  @Nullable
  String description;
  @Nullable
  FeatureVectorSolution solution;
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
        .entityId(stepId.toString())
        .entityClass("Step")
        .entityAction("UPDATE")
        .details(this.toString())
        .principal(updatedBy)
        .build();
  }
}
