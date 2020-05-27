package com.silenteight.sens.webapp.backend.changerequest.create;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.messaging.CreateChangeRequestMessageGateway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateChangeRequestUseCaseTest {

  @Mock
  private AuditTracer auditTracer;
  @Mock
  private CreateChangeRequestMessageGateway messageGateway;

  @InjectMocks
  private CreateChangeRequestUseCase useCase;

  @Test
  void sendsCreateChangeRequestMessage() {
    UUID bulkChangeId = randomUUID();
    String makerComment = "commentABC";
    String makerUsername = "userNameABC";
    OffsetDateTime createdAt = now();

    useCase.apply(CreateChangeRequestCommand.builder()
        .bulkChangeId(bulkChangeId)
        .makerComment(makerComment)
        .makerUsername(makerUsername)
        .createdAt(createdAt)
        .build());

    var messageCaptor = ArgumentCaptor.forClass(
        com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand.class);

    verify(messageGateway).send(messageCaptor.capture());

    var message = messageCaptor.getValue();
    assertThat(message.getBulkChangeId()).isEqualTo(fromJavaUuid(bulkChangeId));
    assertThat(message.getMakerComment()).isEqualTo(makerComment);
    assertThat(message.getMakerUsername()).isEqualTo(makerUsername);
    assertThat(message.getCreatedAt()).isEqualTo(toTimestamp(createdAt));
  }

  @Test
  void savesAuditLogEvent() {
    CreateChangeRequestCommand command =
        new CreateChangeRequestCommand(randomUUID(), "usr1", "comment", now());

    useCase.apply(command);

    verify(auditTracer).save(
        argThat(e -> {
          assertThat(e.getType()).isEqualTo("ChangeRequestCreationRequested");
          assertThat(e.getDetails()).isEqualTo(command);
          assertThat(e.getEntityClass()).isNull();
          assertThat(e.getEntityAction()).isNull();
          assertThat(e.getEntityId()).isNull();
          return true;
        })
    );
  }
}
