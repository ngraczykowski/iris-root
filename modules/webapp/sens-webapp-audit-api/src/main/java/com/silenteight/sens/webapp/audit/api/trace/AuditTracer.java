package com.silenteight.sens.webapp.audit.api.trace;

public interface AuditTracer {

  void save(AuditEvent auditEvent);
}
