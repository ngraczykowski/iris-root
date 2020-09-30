package com.silenteight.sens.webapp.backend.changerequest.cancel;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;

class ChangeRequestCancellationRequestedEvent extends AuditEvent {

  ChangeRequestCancellationRequestedEvent(
      String entityId, String entityClass, Object details) {
    super("ChangeRequestCancellationRequested", entityId, entityClass, UPDATE, details);
  }
}
