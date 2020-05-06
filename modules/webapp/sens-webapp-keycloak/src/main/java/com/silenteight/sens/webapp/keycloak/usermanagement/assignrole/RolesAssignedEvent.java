package com.silenteight.sens.webapp.keycloak.usermanagement.assignrole;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

class RolesAssignedEvent extends AuditEvent {

  RolesAssignedEvent(
      Object details, String entityId, String entityClass, String entityAction) {

    super("RolesAssigned", details, entityId, entityClass, entityAction);
  }
}
