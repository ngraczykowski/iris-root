package com.silenteight.sens.webapp.role.remove;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.sens.webapp.common.audit.AuditableRequest;

import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Value
@Builder
public class RemoveRoleRequest implements AuditableRequest {

  static final String PRE_AUDIT_TYPE = "RemoveRoleRequested";
  static final String POST_AUDIT_TYPE = "RoleRemoved";

  @NonNull
  UUID id;
  @NonNull
  String deletedBy;
  @Builder.Default
  UUID correlationId = randomUUID();

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
        .entityId(id.toString())
        .entityClass("Role")
        .entityAction("DELETE")
        .details(toString())
        .principal(deletedBy)
        .build();
  }
}
