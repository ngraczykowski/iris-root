package com.silenteight.sens.webapp.backend.changerequest.reject;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;

class ChangeRequestRejectionRequestedEvent extends AuditEvent {

  ChangeRequestRejectionRequestedEvent(String entityId, String entityClass, Object details) {
    super("ChangeRequestRejectionRequested", entityId, entityClass, UPDATE, details);
  }
}
