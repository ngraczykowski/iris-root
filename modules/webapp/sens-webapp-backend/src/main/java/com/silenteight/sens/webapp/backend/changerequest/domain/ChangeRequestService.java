package com.silenteight.sens.webapp.backend.changerequest.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotFoundException;

import java.util.UUID;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class ChangeRequestService {

  private final ChangeRequestRepository repository;
  private final AuditTracer auditTracer;

  public void approve(UUID bulkChangeId, String username) {
    log.info(CHANGE_REQUEST,
        "Approving Change Request. bulkChangeId={}, username={}", bulkChangeId, username);

    repository
        .findByBulkChangeId(bulkChangeId)
        .ifPresentOrElse(cr -> this.approve(cr, username), () -> {
          throw new ChangeRequestNotFoundException(bulkChangeId);
        });
  }

  private void approve(ChangeRequest changeRequest, String username) {
    changeRequest.approve(username);
    repository.save(changeRequest);

    log.info(CHANGE_REQUEST, "Change Request approved. changeRequest={}", changeRequest);

    auditTracer.save(
        new ChangeRequestApprovedEvent(
            changeRequest.getBulkChangeId().toString(),
            ChangeRequest.class.getName(),
            changeRequest));
  }
}
