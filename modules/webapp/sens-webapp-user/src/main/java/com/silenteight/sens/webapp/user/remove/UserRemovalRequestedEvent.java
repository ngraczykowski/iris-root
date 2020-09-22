package com.silenteight.sens.webapp.user.remove;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.DELETE;

class UserRemovalRequestedEvent extends AuditEvent {

  UserRemovalRequestedEvent(String entityId, String entityClass, Object details) {
    super("UserRemovalRequested", entityId, entityClass, DELETE, details);
  }
}
