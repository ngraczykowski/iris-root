package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

class ReasoningBranchUpdateRequestedEvent extends AuditEvent {

  ReasoningBranchUpdateRequestedEvent(Object details) {
    super("ReasoningBranchUpdateRequested", details);
  }
}
