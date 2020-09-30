package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.NonNull;

import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

class DiscrepancyArchivingRequestedEvent extends AuditEvent {

  DiscrepancyArchivingRequestedEvent(@NonNull Object details) {
    super("DiscrepancyArchivingRequested", details);
  }
}
