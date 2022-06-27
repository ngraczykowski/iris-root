package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;

class UserCreatedEvent extends AuditEvent {

  UserCreatedEvent(String entityId, String entityClass, Object details) {
    super("UserCreated", entityId, entityClass, CREATE, details);
  }
}
