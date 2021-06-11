package com.silenteight.serp.governance.qa.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;
import com.silenteight.serp.governance.qa.domain.DecisionLevel;
import com.silenteight.serp.governance.qa.domain.DecisionState;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class UpdateDecisionRequest implements AuditableRequest {

  UUID correlationId = randomUUID();
  String entityClass = "Decision";
  @NonNull
  String alertName;
  @NonNull
  DecisionState state;
  @NonNull
  DecisionLevel level;
  @NonNull
  String comment;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(ActionType.UPDATE_DECISION_REQUEST));
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(ActionType.UPDATED_DECISION_REQUEST));
  }

  private AuditDataDto getAuditDataDto(ActionType actionType) {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type(this.getClass().getSimpleName())
        .entityId(alertName)
        .entityClass(entityClass)
        .entityAction(actionType.toString())
        .details(this.toString())
        .principal(createdBy)
        .build();
  }

  enum ActionType {
    UPDATE_DECISION_REQUEST,
    UPDATED_DECISION_REQUEST
  }
}
