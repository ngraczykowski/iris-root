package com.silenteight.sens.webapp.user.lock;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.DELETE;

class UserLockRequestedEvent extends AuditEvent {

  UserLockRequestedEvent(String entityId, String entityClass, Object details) {
    super("UserLockRequested", entityId, entityClass, DELETE, details);
  }
}
