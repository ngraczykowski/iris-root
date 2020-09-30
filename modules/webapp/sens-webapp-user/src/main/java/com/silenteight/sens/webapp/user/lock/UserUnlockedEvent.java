package com.silenteight.sens.webapp.user.lock;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;

class UserUnlockedEvent extends AuditEvent {

  UserUnlockedEvent(String entityId, String entityClass, Object details) {
    super("UserUnlocked", entityId, entityClass, CREATE, details);
  }
}
