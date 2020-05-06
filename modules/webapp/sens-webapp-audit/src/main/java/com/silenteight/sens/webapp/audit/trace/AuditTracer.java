package com.silenteight.sens.webapp.audit.trace;

public interface AuditTracer {

  void save(AuditEvent auditEvent);
}
