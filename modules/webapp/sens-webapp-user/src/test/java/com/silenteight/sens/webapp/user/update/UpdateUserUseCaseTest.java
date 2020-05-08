package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.user.update.UpdateUserUseCaseFixtures.UPDATED_USER;
import static com.silenteight.sens.webapp.user.update.UpdateUserUseCaseFixtures.UPDATE_USER_COMMAND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;
  @Mock
  private AuditTracer auditTracer;

  private UpdateUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserUpdateUseCaseConfiguration().updateUserUseCase(
        updatedUserRepository, roles -> Option.none(), displayName -> Option.none(), auditTracer);
  }

  @Test
  void updateDisplayNameCommand_updateUser() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(UPDATE_USER_COMMAND);

    // then
    verify(updatedUserRepository).save(UPDATED_USER);

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("UserUpdateRequested");
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(UPDATE_USER_COMMAND);
  }
}
