package com.silenteight.sens.webapp.user.lock;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;

class UserUnlockRequestedEvent extends AuditEvent {

  UserUnlockRequestedEvent(String entityId, String entityClass, Object details) {
    super("UserUnlockRequested", entityId, entityClass, CREATE, details);
  }
}
