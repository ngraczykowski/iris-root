package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;

class UserUpdateRequestedEvent extends AuditEvent {

  UserUpdateRequestedEvent(String entityId, String entityClass, Object details) {
    super("UserUpdateRequested", entityId, entityClass, UPDATE, details);
  }
}
