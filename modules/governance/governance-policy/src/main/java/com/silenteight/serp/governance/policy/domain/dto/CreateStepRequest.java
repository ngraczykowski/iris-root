package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;
import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class CreateStepRequest implements AuditableRequest {

  UUID correlationId = UUID.randomUUID();
  @NonNull
  UUID policyId;
  @NonNull
  FeatureVectorSolution solution;
  @NonNull
  UUID stepId;
  @NonNull
  String stepName;
  @Nullable
  String stepDescription;
  @NonNull
  StepType stepType;
  @NonNull
  String createdBy;

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto());
  }

  @Override
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
        .entityAction("CREATE")
        .details(this.toString())
        .principal(createdBy)
        .build();
  }
}
