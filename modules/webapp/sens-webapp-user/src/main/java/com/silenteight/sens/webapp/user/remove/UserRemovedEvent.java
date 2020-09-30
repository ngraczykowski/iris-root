package com.silenteight.sens.webapp.user.remove;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.DELETE;

class UserRemovedEvent extends AuditEvent {

  UserRemovedEvent(String entityId, String entityClass, Object details) {
    super("UserRemoved", entityId, entityClass, DELETE, details);
  }
}
