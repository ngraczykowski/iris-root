package com.silenteight.sens.webapp.user.lock;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.DELETE;

class UserLockedEvent extends AuditEvent {

  UserLockedEvent(String entityId, String entityClass, Object details) {
    super("UserLocked", entityId, entityClass, DELETE, details);
  }
}
