package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;

class RolesAssignedEvent extends AuditEvent {

  RolesAssignedEvent(String entityId, String entityClass, Object details) {
    super("RolesAssigned", entityId, entityClass, UPDATE, details);
  }
}
