package com.silenteight.sens.webapp.backend.changerequest.create;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateChangeRequestUseCaseTest {

  @Mock
  private AuditTracer auditTracer;

  @InjectMocks
  private CreateChangeRequestUseCase useCase;

  @Test
  void savesAuditLogEvent() {
    CreateChangeRequestCommand command =
        new CreateChangeRequestCommand(randomUUID(), "usr1", null, now());

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
