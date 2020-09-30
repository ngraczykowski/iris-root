package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;

class ExternalUserCreationRequestedEvent extends AuditEvent {

  ExternalUserCreationRequestedEvent(String entityId, String entityClass, Object details) {
    super("ExternalUserCreationRequested", entityId, entityClass, CREATE, details);
  }
}
