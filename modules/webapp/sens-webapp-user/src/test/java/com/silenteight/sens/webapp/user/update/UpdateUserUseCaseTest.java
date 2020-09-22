package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;

import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
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
        updatedUserRepository, roles -> Optional.empty(), displayName -> Option.none(),
        auditTracer);
  }

  @Test
  void updateDisplayNameCommand_updateUserInRepo() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(UPDATE_USER_COMMAND);

    // then
    verify(updatedUserRepository).save(UPDATED_USER);


  }

  @Test
  void updateDisplayNameCommand_registerEvents() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(UPDATE_USER_COMMAND);

    // then

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer, times(2)).save(eventCaptor.capture());
    List<AuditEvent> auditEvent = eventCaptor.getAllValues();

    assertThat(auditEvent.get(0).getType()).isEqualTo("UserUpdateRequested");
    assertThat(auditEvent.get(0).getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.get(0).getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.get(0).getDetails()).isEqualTo(UPDATE_USER_COMMAND);

    assertThat(auditEvent.get(1).getType()).isEqualTo("UserUpdated");
    assertThat(auditEvent.get(1).getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.get(1).getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.get(1).getDetails()).isEqualTo(UPDATED_USER);
  }
}
