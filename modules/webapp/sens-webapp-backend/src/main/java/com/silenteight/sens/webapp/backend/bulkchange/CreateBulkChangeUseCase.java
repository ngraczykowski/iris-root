package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@RequiredArgsConstructor
class CreateBulkChangeUseCase {

  private final AuditTracer auditTracer;

  void apply(@NonNull CreateBulkChangeCommand command) {
    log.debug(CHANGE_REQUEST, "Creating Bulk Change requested, command={}", command);

    auditTracer.save(
        new BulkChangeCreationRequestedEvent(command));
  }
}
