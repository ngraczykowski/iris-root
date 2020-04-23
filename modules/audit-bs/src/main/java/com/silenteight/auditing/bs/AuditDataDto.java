package com.silenteight.auditing.bs;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class AuditDataDto {
  @NonNull
  private UUID eventId;
  @NonNull
  private UUID correlationId;
  @NonNull
  private Timestamp timestamp;
  @NonNull
  private String type;
  private String principal;
  private String entityId;
  private String entityClass;
  private String entityAction;
  private String details;
}
