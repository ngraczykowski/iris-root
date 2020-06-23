package com.silenteight.sens.webapp.backend.changerequest.reject;

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
  private RejectChangeRequestMessageGateway messageGateway;


  @Test
  void sendsMessageOverGateway() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(REJECT_COMMAND);

    // then
    var messageCaptor = ArgumentCaptor.forClass(
        com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand.class);

    verify(messageGateway).send(messageCaptor.capture());

    var message = messageCaptor.getValue();
    assertThat(message.getChangeRequestId()).isEqualTo(REJECT_COMMAND.getChangeRequestId());
    assertThat(message.getRejectorUsername()).isEqualTo(REJECT_COMMAND.getRejectorUsername());
    assertThat(message.getRejectorComment()).isEqualTo(REJECT_COMMAND.getRejectorComment());
    assertThat(message.getCorrelationId()).isEqualTo(fromJavaUuid(correlationId));
  }

  @Test
  void savesAuditEvent() {
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
    RejectChangeRequestCommand auditedCommand =
        (RejectChangeRequestCommand) auditEvent.getDetails();
    assertThat(auditedCommand.getChangeRequestId()).isEqualTo(REJECT_COMMAND.getChangeRequestId());
    assertThat(auditedCommand.getRejectorUsername()).isEqualTo(
        REJECT_COMMAND.getRejectorUsername());
    assertThat(auditedCommand.getRejectorComment()).isEqualTo(REJECT_COMMAND.getRejectorComment());
  }
}
