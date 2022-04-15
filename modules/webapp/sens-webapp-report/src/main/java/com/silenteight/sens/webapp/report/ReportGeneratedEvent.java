package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;

class ReportGeneratedEvent extends AuditEvent {

  ReportGeneratedEvent(String entityId, String entityClass, ReportGenerationDetails details) {
    super("ReportGenerated", entityId, entityClass, CREATE, details);
  }
}
