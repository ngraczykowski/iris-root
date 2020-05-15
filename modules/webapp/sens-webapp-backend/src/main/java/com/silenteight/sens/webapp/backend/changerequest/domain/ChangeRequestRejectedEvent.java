package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;

class ChangeRequestRejectedEvent extends AuditEvent {

  ChangeRequestRejectedEvent(String entityId, String entityClass, Object details) {
    super("ChangeRequestRejected", entityId, entityClass, UPDATE, details);
  }
}
