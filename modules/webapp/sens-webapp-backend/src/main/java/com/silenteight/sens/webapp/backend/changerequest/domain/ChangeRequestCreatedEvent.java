package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;

class ChangeRequestCreatedEvent extends AuditEvent  {

  ChangeRequestCreatedEvent(String entityId, String entityClass, Object details) {
    super("ChangeRequestCreated", entityId, entityClass, CREATE, details);
  }
}
