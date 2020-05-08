package com.silenteight.sens.webapp.user.lock;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.CREATE;
import static com.silenteight.sens.webapp.user.lock.UnlockUserUseCaseFixtures.UNLOCK_COMMAND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnlockUserUseCaseTest {

  @Mock
  private UserLocker userLocker;
  @Mock
  private AuditTracer auditTracer;

  private UnlockUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserLockUseCaseConfiguration().unlockUserUseCase(userLocker, auditTracer);
  }

  @Test
  void unlockUserCommand_unlockUserByUsername() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(UNLOCK_COMMAND);

    // then
    verify(userLocker).unlock(UNLOCK_COMMAND.getUsername());

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer, times(2)).save(eventCaptor.capture());
    List<AuditEvent> auditEvents = eventCaptor.getAllValues();

    assertAuditEvent(auditEvents.get(0), "UserUnlockRequested", correlationId);
    assertAuditEvent(auditEvents.get(1), "UserUnlocked", correlationId);
  }

  private void assertAuditEvent(AuditEvent auditEvent, String type, UUID correlationId) {
    assertThat(auditEvent.getType()).isEqualTo(type);
    assertThat(auditEvent.getEntityAction()).isEqualTo(CREATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(UNLOCK_COMMAND);
  }
}
