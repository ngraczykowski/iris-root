package com.silenteight.serp.governance.qa.manage.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;
import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class CreateDecisionRequest implements AuditableRequest {

  UUID correlationId = randomUUID();
  String entityClass = "Decision";
  @NonNull
  String discriminator;
  @NonNull
  DecisionState state;
  @NonNull
  DecisionLevel level;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(ActionType.CREATE_DECISION_REQUEST));
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(ActionType.DECISION_CREATED_REQUEST));
  }

  private AuditDataDto getAuditDataDto(ActionType actionType) {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(from(now()))
        .type(this.getClass().getSimpleName())
        .entityId(discriminator)
        .entityClass(entityClass)
        .entityAction(actionType.toString())
        .details(this.toString())
        .principal(createdBy)
        .build();
  }

  enum ActionType {
    CREATE_DECISION_REQUEST,
    DECISION_CREATED_REQUEST
  }

  public AlertDto toAlertDto() {
    return AlertDto.builder()
        .discriminator(getDiscriminator())
        .level(getLevel())
        .state(getState())
        .build();
  }
}
