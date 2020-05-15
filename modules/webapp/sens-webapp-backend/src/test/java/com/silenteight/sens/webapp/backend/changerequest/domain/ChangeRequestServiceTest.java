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

  @InjectMocks
  private ChangeRequestService underTest;

  @Mock
  private ChangeRequestRepository repository;

  @Mock
  private AuditTracer auditTracer;

  @Test
  void changeRequestNotFound_throwChangeRequestNotFoundException() {
    // given
    String approverUsername = "approver";
    given(repository.findByBulkChangeId(BULK_CHANGE_ID)).willReturn(empty());

    // when
    Executable when = () -> underTest.approve(BULK_CHANGE_ID, approverUsername);

    // then
    assertThrows(ChangeRequestNotFoundException.class, when);
  }

  @Test
  void changeRequestFound_approveChangeRequest() {
    // given
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);
    String approverUsername = "approver";
    given(repository.findByBulkChangeId(BULK_CHANGE_ID)).willReturn(of(changeRequest));

    // when
    underTest.approve(BULK_CHANGE_ID, approverUsername);

    // then
    ArgumentCaptor<ChangeRequest> changeRequestCaptor =
        ArgumentCaptor.forClass(ChangeRequest.class);
    verify(repository).save(changeRequestCaptor.capture());
    ChangeRequest changeRequestCaptured = changeRequestCaptor.getValue();
    assertThat(changeRequestCaptured.getState()).isEqualTo(APPROVED.toString());
    assertThat(changeRequestCaptured.getApproverUsername()).isEqualTo(approverUsername);

    UUID correlationId = RequestCorrelation.id();
    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("ChangeRequestApproved");
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(changeRequest);
  }
}
