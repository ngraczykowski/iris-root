package com.silenteight.sens.webapp.backend.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotAllowedException;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class ChangeRequestService {

  private static final String CHANGE_REQUEST_ENTITY_NAME = "webapp_change_request";

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
            changeRequest.getId().toString(),
            CHANGE_REQUEST_ENTITY_NAME,
            changeRequest));
  }

  public UUID approve(
      long id,
      @NonNull String username,
      @NonNull String comment,
      @NonNull OffsetDateTime approvedAt) {
    log.info(CHANGE_REQUEST,
        "Approving Change Request. changeRequestId={}, username={}, comment={}, approvedAt={}",
        id, username, comment, approvedAt);

    ChangeRequest changeRequest = repository.getById(id);

    if (changeRequest.getCreatedBy().equals(username)) {
      throw new ChangeRequestNotAllowedException(id);
    }

    changeRequest.approve(username, comment, approvedAt);
    repository.save(changeRequest);

    log.info(CHANGE_REQUEST, "Change Request approved. changeRequest={}", changeRequest);

    auditTracer.save(
        new ChangeRequestApprovedEvent(
            changeRequest.getId().toString(),
            CHANGE_REQUEST_ENTITY_NAME,
            changeRequest));

    return changeRequest.getBulkChangeId();
  }

  public UUID reject(
      long id,
      @NonNull String username,
      @NonNull String comment,
      @NonNull OffsetDateTime rejectedAt) {
    log.info(CHANGE_REQUEST,
        "Rejecting Change Request. changeRequestId={}, username={}, comment={}, rejectedAt={}",
        id, username, comment, rejectedAt);

    ChangeRequest changeRequest = repository.getById(id);

    if (changeRequest.getCreatedBy().equals(username)) {
      throw new ChangeRequestNotAllowedException(id);
    }

    changeRequest.reject(username, comment, rejectedAt);
    repository.save(changeRequest);

    log.info(CHANGE_REQUEST, "Change Request rejected. changeRequest={}", changeRequest);

    auditTracer.save(
        new ChangeRequestRejectedEvent(
            changeRequest.getId().toString(),
            CHANGE_REQUEST_ENTITY_NAME,
            changeRequest));

    return changeRequest.getBulkChangeId();
  }

  public UUID cancel(
      long id,
      @NonNull String username,
      @NonNull String comment,
      @NonNull OffsetDateTime cancelledAt) {
    log.info(CHANGE_REQUEST,
        "Cancelling Change Request. changeRequestId={}, username={}, comment={}, rejectedAt={}",
        id, username, comment, cancelledAt);

    ChangeRequest changeRequest = repository.getById(id);
    changeRequest.cancel(username, comment, cancelledAt);
    repository.save(changeRequest);

    log.info(CHANGE_REQUEST, "Change Request cancelled. changeRequest={}", changeRequest);

    auditTracer.save(
        new ChangeRequestCancelledEvent(
            changeRequest.getId().toString(),
            CHANGE_REQUEST_ENTITY_NAME,
            changeRequest));

    return changeRequest.getBulkChangeId();
  }
}
