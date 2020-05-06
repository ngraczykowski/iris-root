package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

class KeycloakUserCreatedEvent extends AuditEvent {

  KeycloakUserCreatedEvent(
      Object details, String entityId, String entityClass, String entityAction) {
    super("KeycloakUserCreated", details, entityId, entityClass, entityAction);
  }
}
