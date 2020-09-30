package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;

class ChangeRequestApprovedEvent extends AuditEvent {

  ChangeRequestApprovedEvent(String entityId, String entityClass, Object details) {
    super("ChangeRequestApproved", entityId, entityClass, UPDATE, details);
  }
}
