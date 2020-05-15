package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeRequestServiceTest {

  private static final UUID BULK_CHANGE_ID = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String MAKER_USERNAME = "maker";
  private static final String MAKER_COMMENT = "This is comment from Maker";
  private static final String APPROVER_USERNAME = "approver";

  @InjectMocks
  private ChangeRequestService underTest;

  @Mock
  private ChangeRequestRepository repository;

  @Mock
  private AuditTracer auditTracer;

  @Test
  void changeRequestNotFoundWhenApproving_throwChangeRequestNotFoundException() {
    // given
    given(repository.findByBulkChangeId(BULK_CHANGE_ID)).willReturn(empty());

    // when
    Executable when = () -> underTest.approve(BULK_CHANGE_ID, APPROVER_USERNAME);

    // then
    assertThrows(ChangeRequestNotFoundException.class, when);
  }

  @Test
  void changeRequestFound_approveChangeRequest() {
    // given
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);
    given(repository.findByBulkChangeId(BULK_CHANGE_ID)).willReturn(of(changeRequest));

    // when
    underTest.approve(BULK_CHANGE_ID, APPROVER_USERNAME);

    // then
    verifyChangeRequest(APPROVED, APPROVER_USERNAME);
    verifyAuditLog("ChangeRequestApproved", changeRequest);
  }

  @Test
  void changeRequestNotFoundWhenRejecting_throwChangeRequestNotFoundException() {
    // given
    given(repository.findByBulkChangeId(BULK_CHANGE_ID)).willReturn(empty());

    // when
    Executable when = () -> underTest.reject(BULK_CHANGE_ID, APPROVER_USERNAME);

    // then
    assertThrows(ChangeRequestNotFoundException.class, when);
  }

  @Test
  void changeRequestFound_rejectChangeRequest() {
    // given
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);
    given(repository.findByBulkChangeId(BULK_CHANGE_ID)).willReturn(of(changeRequest));

    // when
    underTest.reject(BULK_CHANGE_ID, APPROVER_USERNAME);

    // then
    verifyChangeRequest(REJECTED, APPROVER_USERNAME);
    verifyAuditLog("ChangeRequestRejected", changeRequest);
  }

  private void verifyChangeRequest(ChangeRequestState expectedState, String approverUsername) {
    ArgumentCaptor<ChangeRequest> changeRequestCaptor =
        ArgumentCaptor.forClass(ChangeRequest.class);
    verify(repository).save(changeRequestCaptor.capture());
    ChangeRequest changeRequest = changeRequestCaptor.getValue();
    assertThat(changeRequest.getState()).isEqualTo(expectedState.toString());
    assertThat(changeRequest.getApproverUsername()).isEqualTo(approverUsername);
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
