package com.silenteight.sens.webapp.user.password.reset;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;

class PasswordResetEvent extends AuditEvent {

  PasswordResetEvent(String entityId, String entityClass) {
    super("PasswordReset", entityId, entityClass, UPDATE);
  }
}
