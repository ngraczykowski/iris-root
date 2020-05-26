package com.silenteight.sens.webapp.backend.changerequest.create;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

class ChangeRequestCreationRequestedEvent extends AuditEvent {

  ChangeRequestCreationRequestedEvent(Object details) {
    super("ChangeRequestCreationRequested", details);
  }
}
