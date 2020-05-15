package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;

class ChangeRequestApprovalRequestedEvent extends AuditEvent {

  ChangeRequestApprovalRequestedEvent(String entityId, String entityClass, Object details) {
    super("ChangeRequestApprovalRequested", entityId, entityClass, UPDATE, details);
  }
}
