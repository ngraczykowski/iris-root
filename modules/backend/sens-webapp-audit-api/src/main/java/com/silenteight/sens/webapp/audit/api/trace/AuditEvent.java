package com.silenteight.sens.webapp.audit.api.trace;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

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
  private final String type;
  @Nullable
  private final String principal;
  @Nullable
  private final String entityId;
  @Nullable
  private final String entityClass;
  @Nullable
  private final String entityAction;
  @NonNull
  private final Object details;

  public AuditEvent(String type) {
    this(type, null);
  }

  public AuditEvent(String type, Object details) {
    this(type, null, null, null, details);
  }

  public AuditEvent(
      String type,
      String entityId,
      String entityClass,
      EntityAction entityAction) {

    this(type, entityId, entityClass, entityAction, null);
  }

  public AuditEvent(
      String type,
      String entityId,
      String entityClass,
      EntityAction entityAction,
      Object details) {

    this.id = UUID.randomUUID();
    this.correlationId = RequestCorrelation.id();
    this.timestamp = Instant.now();
    this.type = type;
    this.principal = getPrincipalFromSecurityContext();
    this.entityId = entityId;
    this.entityClass = entityClass;
    this.entityAction = ofNullable(entityAction).map(Object::toString).orElse(null);
    this.details = details;
  }

  private static String getPrincipalFromSecurityContext() {
    return ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(AuditEvent::getPrincipalFromAuthentication)
        .orElse(null);
  }

  private static String getPrincipalFromAuthentication(Authentication authentication) {
    Object principal = authentication.getPrincipal();

    if (principal instanceof UserDetails)
      return ((UserDetails) principal).getUsername();
    else
      return principal.toString();
  }

  public enum EntityAction {

    CREATE, UPDATE, DELETE;
  }
}
