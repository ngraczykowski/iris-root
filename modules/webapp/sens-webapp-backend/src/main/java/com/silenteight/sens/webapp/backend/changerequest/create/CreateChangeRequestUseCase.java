package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class CreateChangeRequestUseCase {

  private final AuditTracer auditTracer;

  public void apply(@NonNull CreateChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Creating Change Request requested, command={}", command);

    auditTracer.save(new ChangeRequestCreationRequestedEvent(command));
  }
}
