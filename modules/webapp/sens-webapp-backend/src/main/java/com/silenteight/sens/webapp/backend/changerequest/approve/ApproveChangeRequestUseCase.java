package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.lang.String.valueOf;

@Slf4j
@RequiredArgsConstructor
public class ApproveChangeRequestUseCase {

  @NonNull
  private final AuditTracer auditTracer;

  public void apply(@NonNull ApproveChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Approving Change Request, command={}", command);

    auditTracer.save(
        new ChangeRequestApprovalRequestedEvent(
            valueOf(command.getChangeRequestId()),
            "webapp_change_request",
            command));

    log.debug(CHANGE_REQUEST,
        "Approved Change Request, changeRequestId={}", command.getChangeRequestId());
  }
}
