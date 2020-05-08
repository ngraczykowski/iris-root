package com.silenteight.sens.webapp.user.password.reset;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;

class PasswordResetRequestedEvent extends AuditEvent {

  PasswordResetRequestedEvent(String entityId, String entityClass) {
    super("PasswordResetRequested", entityId, entityClass, UPDATE);
  }
}
