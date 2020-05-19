package com.silenteight.sens.webapp.backend.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotFoundException;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class ChangeRequestService {

  @NonNull
  private final ChangeRequestRepository repository;
  @NonNull
  private final AuditTracer auditTracer;

  public void create(
      UUID bulkChangeId, String username, String comment, OffsetDateTime creationDate) {
    log.info(CHANGE_REQUEST, "Creating Change Request. bulkChangeId={}, username={}, comment={}",
        bulkChangeId, username, comment);

    ChangeRequest changeRequest = new ChangeRequest(bulkChangeId, username, comment, creationDate);
    repository.save(changeRequest);

    auditTracer.save(
        new ChangeRequestCreatedEvent(
            bulkChangeId.toString(),
            ChangeRequest.class.getName(),
            changeRequest));
  }

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

  public void reject(UUID bulkChangeId, String username) {
    log.info(CHANGE_REQUEST,
        "Rejecting Change Request. bulkChangeId={}, username={}", bulkChangeId, username);

    repository
        .findByBulkChangeId(bulkChangeId)
        .ifPresentOrElse(cr -> this.reject(cr, username), () -> {
          throw new ChangeRequestNotFoundException(bulkChangeId);
        });
  }

  private void reject(ChangeRequest changeRequest, String username) {
    changeRequest.reject(username);
    repository.save(changeRequest);

    log.info(CHANGE_REQUEST, "Change Request rejected. changeRequest={}", changeRequest);

    auditTracer.save(
        new ChangeRequestRejectedEvent(
            changeRequest.getBulkChangeId().toString(),
            ChangeRequest.class.getName(),
            changeRequest));
  }
}
