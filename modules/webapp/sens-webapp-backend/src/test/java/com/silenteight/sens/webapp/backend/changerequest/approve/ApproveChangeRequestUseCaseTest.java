package com.silenteight.sens.webapp.backend.changerequest.approve;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestUseCaseFixtures.APPROVE_COMMAND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApproveChangeRequestUseCaseTest {

  @InjectMocks
  private ApproveChangeRequestUseCase underTest;

  @Mock
  private AuditTracer auditTracer;
  @Mock
  private ApproveChangeRequestMessageGateway messageGateway;

  @Test
  void approveChangeRequestCommand_approveChangeRequest() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(APPROVE_COMMAND);

    // then
    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("ChangeRequestApprovalRequested");
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isInstanceOf(ApproveChangeRequestCommand.class);
    ApproveChangeRequestCommand command =
        (ApproveChangeRequestCommand) auditEvent.getDetails();
    assertThat(command.getChangeRequestId()).isEqualTo(APPROVE_COMMAND.getChangeRequestId());
    assertThat(command.getApproverUsername()).isEqualTo(APPROVE_COMMAND.getApproverUsername());
    assertThat(command.getApproverComment()).isEqualTo(APPROVE_COMMAND.getApproverComment());

    var messageCaptor = ArgumentCaptor.forClass(
        com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand.class);

    verify(messageGateway).send(messageCaptor.capture());

    var message = messageCaptor.getValue();
    assertThat(message.getChangeRequestId()).isEqualTo(APPROVE_COMMAND.getChangeRequestId());
    assertThat(message.getApproverUsername()).isEqualTo(APPROVE_COMMAND.getApproverUsername());
    assertThat(message.getApproverComment()).isEqualTo(APPROVE_COMMAND.getApproverComment());
    assertThat(message.getCorrelationId()).isEqualTo(fromJavaUuid(correlationId));
  }
}
