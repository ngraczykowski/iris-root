package com.silenteight.sens.webapp.keycloak.usermanagement.update;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

class KeycloakUserUpdatedEvent extends AuditEvent {

  KeycloakUserUpdatedEvent(
      Object details, String entityId, String entityClass, String entityAction) {
    super("KeycloakUserUpdated", details, entityId, entityClass, entityAction);
  }
}
