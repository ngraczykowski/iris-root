package com.silenteight.sens.webapp.audit.trace;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class Slf4jAuditTracer implements AuditTracer {

  @Override
  public void save(AuditEvent auditEvent) {
    log.info("Audit event raised: {}", auditEvent);
  }
}
