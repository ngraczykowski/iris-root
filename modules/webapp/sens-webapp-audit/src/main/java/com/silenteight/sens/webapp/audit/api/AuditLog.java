package com.silenteight.sens.webapp.audit.api;

import static com.silenteight.sens.webapp.audit.api.SeverityLevel.ERROR;
import static com.silenteight.sens.webapp.audit.api.SeverityLevel.INFO;

public interface AuditLog {

  void log(SeverityLevel severityLevel, AuditMarker auditMarker, String format, Object... args);

  default void logInfo(AuditMarker auditMarker, String format, Object... args) {
    log(INFO, auditMarker, format, args);
  }

  default void logError(AuditMarker auditMarker, String format, Object... args) {
    log(ERROR, auditMarker, format, args);
  }
}
