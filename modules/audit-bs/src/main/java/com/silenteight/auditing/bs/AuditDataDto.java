package com.silenteight.auditing.bs;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.sql.Timestamp;
import java.util.UUID;

@Value
@Builder
public class AuditDataDto {

  @NonNull
  UUID eventId;

  @NonNull
  UUID correlationId;

  @NonNull
  Timestamp timestamp;

  @NonNull
  String type;

  String principal;

  String entityId;

  String entityClass;

  String entityAction;

  String details;
}
