package com.silenteight.sens.webapp.audit.api.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class AuditLogDto {

  @NonNull
  UUID eventId;
  @NonNull
  UUID correlationId;
  @NonNull
  OffsetDateTime timestamp;
  @NonNull
  String type;
  String principal;
  String entityId;
  String entityClass;
  String entityAction;
  String details;
}
