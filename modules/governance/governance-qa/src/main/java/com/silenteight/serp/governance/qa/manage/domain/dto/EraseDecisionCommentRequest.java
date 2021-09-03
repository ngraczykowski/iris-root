package com.silenteight.serp.governance.qa.manage.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;
import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class EraseDecisionCommentRequest implements AuditableRequest {

  static final String PRE_AUDIT_TYPE = "EraseDecisionCommentRequested";
  static final String POST_AUDIT_TYPE = "DecisionCommentErased";

  UUID correlationId = randomUUID();
  @NonNull
  String discriminator;
  @NonNull
  DecisionLevel level;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(PRE_AUDIT_TYPE));
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(POST_AUDIT_TYPE));
  }

  private AuditDataDto getAuditDataDto(String type) {
    return AuditDataDto.builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(from(now()))
        .type(type)
        .entityId(discriminator)
        .entityClass("Decision")
        .entityAction("UPDATE")
        .details(toString())
        .principal(createdBy)
        .build();
  }
}
