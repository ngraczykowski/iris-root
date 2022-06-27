package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;

class RolesAssignedEvent extends AuditEvent {

  RolesAssignedEvent(String entityId, String entityClass, Object details) {
    super("RolesAssigned", entityId, entityClass, UPDATE, details);
  }
}
