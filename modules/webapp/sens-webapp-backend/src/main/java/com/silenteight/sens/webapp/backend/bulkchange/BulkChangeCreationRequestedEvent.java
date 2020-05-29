package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

class BulkChangeCreationRequestedEvent extends AuditEvent {

  BulkChangeCreationRequestedEvent(Object details) {
    super("BulkChangeCreationRequested", details);
  }
}
