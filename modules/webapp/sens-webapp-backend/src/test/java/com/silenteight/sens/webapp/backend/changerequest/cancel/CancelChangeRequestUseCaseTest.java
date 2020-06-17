package com.silenteight.sens.webapp.backend.changerequest.cancel;

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
import static com.silenteight.sens.webapp.backend.changerequest.cancel.CancelChangeRequestUseCaseFixtures.CANCEL_COMMAND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelChangeRequestUseCaseTest {

  @InjectMocks
  private CancelChangeRequestUseCase underTest;

  @Mock
  private AuditTracer auditTracer;
  @Mock
  private CancelChangeRequestMessageGateway messageGateway;

  @Test
  void cancelChangeRequestCommand_cancelChangeRequest() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(CANCEL_COMMAND);

    // then
    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("ChangeRequestCancellationRequested");
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isInstanceOf(CancelChangeRequestCommand.class);
    CancelChangeRequestCommand command =
        (CancelChangeRequestCommand) auditEvent.getDetails();
    assertThat(command.getChangeRequestId()).isEqualTo(CANCEL_COMMAND.getChangeRequestId());
    assertThat(command.getCancellerUsername()).isEqualTo(CANCEL_COMMAND.getCancellerUsername());

    var messageCaptor = ArgumentCaptor.forClass(
        com.silenteight.proto.serp.v1.changerequest.CancelChangeRequestCommand.class);

    verify(messageGateway).send(messageCaptor.capture());

    var message = messageCaptor.getValue();
    assertThat(message.getChangeRequestId()).isEqualTo(CANCEL_COMMAND.getChangeRequestId());
    assertThat(message.getCancellerUsername()).isEqualTo(CANCEL_COMMAND.getCancellerUsername());
    assertThat(message.getCorrelationId()).isEqualTo(fromJavaUuid(correlationId));
  }
}
