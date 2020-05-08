package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.audit.trace.AuditEvent;

class AnalystsSyncRequestedEvent extends AuditEvent {

  AnalystsSyncRequestedEvent() {
    super("AnalystsSyncRequested");
  }
}
