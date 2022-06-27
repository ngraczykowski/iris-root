package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;

class InternalUserCreationRequestedEvent extends AuditEvent {

  InternalUserCreationRequestedEvent(String entityId, String entityClass, Object details) {
    super("InternalUserCreationRequested", entityId, entityClass, CREATE, details);
  }
}
