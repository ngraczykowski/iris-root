package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;

class ChangeRequestCancelledEvent extends AuditEvent {

  ChangeRequestCancelledEvent(String entityId, String entityClass, Object details) {
    super("ChangeRequestCancelled", entityId, entityClass, UPDATE, details);
  }
}
