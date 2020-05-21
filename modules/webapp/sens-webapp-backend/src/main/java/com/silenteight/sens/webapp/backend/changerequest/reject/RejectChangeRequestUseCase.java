package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.lang.String.valueOf;

@Slf4j
@RequiredArgsConstructor
public class RejectChangeRequestUseCase {

  @NonNull
  private final AuditTracer auditTracer;

  public void apply(@NonNull RejectChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Rejecting Change Request, command={}", command);

    auditTracer.save(
        new ChangeRequestRejectionRequestedEvent(
            valueOf(command.getChangeRequestId()),
            "webapp_change_request",
            command));


    log.debug(CHANGE_REQUEST,
        "Rejected Change Request, changeRequestId={}", command.getChangeRequestId());
  }
}
