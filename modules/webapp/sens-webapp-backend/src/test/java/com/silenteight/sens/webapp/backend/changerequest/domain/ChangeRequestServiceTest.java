package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotAllowedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;
import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;
import static java.time.OffsetDateTime.parse;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeRequestServiceTest {

  private static final long CHANGE_REQUEST_ID = 1L;
  private static final UUID BULK_CHANGE_ID = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String MAKER_USERNAME = "maker";
  private static final String MAKER_COMMENT = "This is comment from Maker";
  private static final String APPROVER_USERNAME = "approver";
  private static final String APPROVER_COMMENT = "approver comment";
  private static final OffsetDateTime CREATION_DATE = parse("2020-05-20T10:15:30+01:00");
  private static final OffsetDateTime APPROVED_AT = parse("2020-05-21T12:10:35+01:00");
  private static final OffsetDateTime REJECTED_AT = parse("2020-05-21T12:10:35+01:00");

  private ChangeRequestService underTest;

  private final InMemoryChangeRequestRepository repository = new InMemoryChangeRequestRepository();

  @Mock
  private AuditTracer auditTracer;

  @BeforeEach
  void setUp() {
    underTest = new ChangeRequestConfiguration().changeRequestService(repository, auditTracer);
  }

  @Test
  void invokesRepositoryToSaveNewChangeRequestOnCreate() {
    underTest.create(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT, CREATION_DATE);

    List<ChangeRequest> allByState = repository.findAllByState(PENDING);
    assertThat(allByState).hasSize(1);
    ChangeRequest changeRequest = allByState.get(0);
    assertThat(changeRequest.getBulkChangeId()).isEqualTo(BULK_CHANGE_ID);
    assertThat(changeRequest.getCreatedBy()).isEqualTo(MAKER_USERNAME);
    assertThat(changeRequest.getCreatorComment()).isEqualTo(MAKER_COMMENT);
    assertThat(changeRequest.getCreatedAt()).isEqualTo(CREATION_DATE);
    assertThat(changeRequest.getState()).isEqualTo(PENDING);
  }

  @Test
  void createsAuditEventOnCreate() {
    UUID correlationId = RequestCorrelation.id();

    underTest.create(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT, CREATION_DATE);

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getEntityId()).isEqualTo(
        repository.getByBulkChangeId(BULK_CHANGE_ID).getId().toString());
    assertThat(auditEvent.getType()).isEqualTo("ChangeRequestCreated");
    assertThat(auditEvent.getEntityAction()).isEqualTo(CREATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isInstanceOf(ChangeRequest.class);
    ChangeRequest auditDetails = (ChangeRequest) auditEvent.getDetails();
    assertThat(auditDetails.getBulkChangeId()).isEqualTo(BULK_CHANGE_ID);
    assertThat(auditDetails.getCreatedBy()).isEqualTo(MAKER_USERNAME);
    assertThat(auditDetails.getCreatorComment()).isEqualTo(MAKER_COMMENT);
    assertThat(auditDetails.getCreatedAt()).isEqualTo(CREATION_DATE);
    assertThat(auditDetails.getState()).isEqualTo(PENDING);
  }

  @Test
  void changeRequestNotFoundWhenApproving_throwChangeRequestNotFoundException() {
    // when
    Executable when =
        () -> underTest.approve(CHANGE_REQUEST_ID, APPROVER_USERNAME, APPROVER_COMMENT, APPROVED_AT);

    // then
    assertThrows(NullPointerException.class, when);
  }

  @Test
  void changeRequestFound_approveChangeRequest() {
    // given
    ChangeRequest changeRequest = makeChangeRequest();
    repository.save(changeRequest);

    // when
    underTest.approve(CHANGE_REQUEST_ID, APPROVER_USERNAME, APPROVER_COMMENT, APPROVED_AT);

    // then
    ChangeRequest repositoryValue = repository.getById(CHANGE_REQUEST_ID);

    assertThat(repositoryValue.getState()).isEqualTo(APPROVED);
    assertThat(repositoryValue.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
    assertThat(repositoryValue.getDeciderComment()).isEqualTo(APPROVER_COMMENT);
    assertThat(repositoryValue.getDecidedAt()).isEqualTo(APPROVED_AT);
    verifyAuditLog("ChangeRequestApproved", changeRequest);
  }

  @Test
  void changeRequestApprovedByMaker_throwChangeRequestNotAllowed() {
    // given
    ChangeRequest changeRequest = makeChangeRequest();
    repository.save(changeRequest);

    // when
    Executable when =
        () -> underTest.approve(CHANGE_REQUEST_ID, MAKER_USERNAME, APPROVER_COMMENT, APPROVED_AT);

    // then
    assertThrows(ChangeRequestNotAllowedException.class, when);
  }

  @Test
  void changeRequestNotFoundWhenRejecting_throwChangeRequestNotFoundException() {
    // when
    Executable when =
        () -> underTest.reject(CHANGE_REQUEST_ID, APPROVER_USERNAME, APPROVER_COMMENT, REJECTED_AT);

    // then
    assertThrows(NullPointerException.class, when);
  }

  @Test
  void changeRequestFound_rejectChangeRequest() {
    // given
    ChangeRequest changeRequest = makeChangeRequest();
    repository.save(changeRequest);

    // when
    underTest.reject(CHANGE_REQUEST_ID, APPROVER_USERNAME, APPROVER_COMMENT, REJECTED_AT);

    // then
    ChangeRequest repositoryValue = repository.getById(CHANGE_REQUEST_ID);
    assertThat(repositoryValue.getState()).isEqualTo(REJECTED);
    assertThat(repositoryValue.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
    assertThat(repositoryValue.getDeciderComment()).isEqualTo(APPROVER_COMMENT);
    assertThat(repositoryValue.getDecidedAt()).isEqualTo(REJECTED_AT);
    verifyAuditLog("ChangeRequestRejected", changeRequest);
  }


  @Test
  void changeRequestRejectedByMaker_throwChangeRequestNotAllowed() {
    // given
    ChangeRequest changeRequest = makeChangeRequest();
    repository.save(changeRequest);

    // when
    Executable when =
        () -> underTest.reject(CHANGE_REQUEST_ID, MAKER_USERNAME, APPROVER_COMMENT, REJECTED_AT);

    // then
    assertThrows(ChangeRequestNotAllowedException.class, when);
  }

  @Test
  void changeRequestFound_cancelChangeRequest() {
    // given
    OffsetDateTime cancelledAt = parse("2020-05-21T10:15:30+01:00");
    ChangeRequest changeRequest = makeChangeRequest();
    repository.save(changeRequest);

    // when
    underTest.cancel(CHANGE_REQUEST_ID, APPROVER_USERNAME, APPROVER_COMMENT, cancelledAt);

    // then
    ChangeRequest repositoryValue = repository.getById(CHANGE_REQUEST_ID);
    assertThat(repositoryValue.getState()).isEqualTo(CANCELLED);
    assertThat(repositoryValue.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
    assertThat(repositoryValue.getDeciderComment()).isEqualTo(APPROVER_COMMENT);
    assertThat(repositoryValue.getDecidedAt()).isEqualTo(cancelledAt);
    verifyAuditLog("ChangeRequestCancelled", changeRequest);
  }

  private static ChangeRequest makeChangeRequest() {
    ChangeRequest changeRequest = new ChangeRequest(
        BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT, CREATION_DATE);
    changeRequest.setId(CHANGE_REQUEST_ID);

    return changeRequest;
  }

  private void verifyChangeRequest(
      long changeRequestId,
      ChangeRequestState expectedState,
      String approverUsername,
      OffsetDateTime updatedAt) {

    ChangeRequest repositoryValue = repository.getById(changeRequestId);
    ChangeRequest changeRequest = repositoryValue;
    assertThat(changeRequest.getState()).isEqualTo(expectedState);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(approverUsername);
    assertThat(changeRequest.getDecidedAt()).isEqualTo(updatedAt);
  }

  private void verifyAuditLog(String type, ChangeRequest details) {
    UUID correlationId = RequestCorrelation.id();

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo(type);
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(details);
  }
}
