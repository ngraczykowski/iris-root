package com.silenteight.sens.webapp.role.edit;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import static java.sql.Timestamp.from;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Value
@Builder
public class EditRoleRequest implements AuditableRequest {

  static final String PRE_AUDIT_TYPE = "UpdateRoleRequested";
  static final String POST_AUDIT_TYPE = "RoleUpdated";

  @NonNull
  UUID id;
  @Nullable
  String name;
  @Nullable
  String description;
  @Nullable
  Set<UUID> permissions;
  @NonNull
  String updatedBy;
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
        .entityAction("UPDATE")
        .details(toString())
        .principal(updatedBy)
        .build();
  }
}
