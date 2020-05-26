package com.silenteight.sens.webapp.backend.changerequest.reject;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.messaging.RejectChangeRequestMessageSender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestUseCaseFixtures.REJECT_COMMAND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RejectChangeRequestUseCaseTest {

  @InjectMocks
  private RejectChangeRequestUseCase underTest;

  @Mock
  private AuditTracer auditTracer;
  @Mock
  private RejectChangeRequestMessageSender messageSender;

  @Test
  void rejectChangeRequestCommand_rejectChangeRequest() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(REJECT_COMMAND);

    // then
    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("ChangeRequestRejectionRequested");
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isInstanceOf(RejectChangeRequestCommand.class);
    RejectChangeRequestCommand command =
        (RejectChangeRequestCommand) auditEvent.getDetails();
    assertThat(command.getChangeRequestId()).isEqualTo(REJECT_COMMAND.getChangeRequestId());
    assertThat(command.getRejectorUsername()).isEqualTo(REJECT_COMMAND.getRejectorUsername());

    verify(messageSender).send(
        any(com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand.class));
  }
}
