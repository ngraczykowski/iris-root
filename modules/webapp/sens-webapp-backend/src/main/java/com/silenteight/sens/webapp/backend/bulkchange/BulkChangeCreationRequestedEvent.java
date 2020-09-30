package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

class BulkChangeCreationRequestedEvent extends AuditEvent {

  BulkChangeCreationRequestedEvent(Object details) {
    super("BulkChangeCreationRequested", details);
  }
}
