package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.CREATE;

public class BulkChangeCreationRequestedEvent extends AuditEvent {

  public BulkChangeCreationRequestedEvent(String entityId, Object details) {
    super("ChangeRequestApprovalRequested", entityId, "bulk_change", CREATE, details);
  }
}
