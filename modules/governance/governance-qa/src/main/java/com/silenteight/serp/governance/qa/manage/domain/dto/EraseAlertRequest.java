package com.silenteight.serp.governance.qa.manage.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static java.sql.Timestamp.from;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class EraseAlertRequest implements AuditableRequest {

  UUID correlationId = randomUUID();
  String entityClass = "Alert";
  long alertId;
  @NonNull
  String deletedBy;
  @NonNull
  OffsetDateTime deletedAt;

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(ActionType.DELETE_ALERT_REQUEST));
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(ActionType.ALERT_DELETED_REQUEST));
  }

  private AuditDataDto getAuditDataDto(ActionType actionType) {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(from(deletedAt.toInstant()))
        .type(getClass().getSimpleName())
        .entityId(valueOf(alertId))
        .entityClass(entityClass)
        .entityAction(actionType.toString())
        .details(toString())
        .principal(deletedBy)
        .build();
  }

  enum ActionType {
    DELETE_ALERT_REQUEST,
    ALERT_DELETED_REQUEST
  }
}
