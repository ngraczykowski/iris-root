package com.silenteight.serp.governance.changerequest.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@Value
@Builder
public class CancelChangeRequestRequest implements AuditableRequest {

  public static final String PRE_AUDIT_TYPE = "CancelChangeRequestRequested";
  public static final String POST_AUDIT_TYPE = "ChangeRequestCancelled";

  @NonNull
  UUID correlationId;
  @NonNull
  UUID changeRequestId;
  @NonNull
  String cancelledBy;

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
        .entityId(changeRequestId.toString())
        .entityClass("ChangeRequest")
        .entityAction("UPDATE")
        .details(toString())
        .principal(cancelledBy)
        .build();
  }
}
