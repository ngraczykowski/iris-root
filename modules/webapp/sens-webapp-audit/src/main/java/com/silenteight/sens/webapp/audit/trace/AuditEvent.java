package com.silenteight.sens.webapp.audit.trace;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;

import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

@Getter
@ToString
public class AuditEvent {

  @NonNull
  private final UUID id;
  @NonNull
  private final UUID correlationId;
  @NonNull
  private final Instant timestamp;
  @NonNull
  private final Object details;
  @NonNull
  private final String type;
  @Nullable
  private final String entityId;
  @Nullable
  private final String entityClass;
  @Nullable
  private final String entityAction;

  public AuditEvent(String type, Object details) {
    this(type, details, null, null, null);
  }

  public AuditEvent(
      String type,
      Object details,
      String entityId,
      String entityClass,
      String entityAction) {

    this.id = UUID.randomUUID();
    this.correlationId = RequestCorrelation.id();
    this.timestamp = Instant.now();
    this.details = details;
    this.type = type;
    this.entityId = entityId;
    this.entityClass = entityClass;
    this.entityAction = entityAction;
  }
}
