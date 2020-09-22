package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;

class UserUpdatedEvent extends AuditEvent {

  UserUpdatedEvent(String entityId, String entityClass, Object details) {
    super("UserUpdated", entityId, entityClass, UPDATE, details);
  }
}
