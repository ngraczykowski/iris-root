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

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.DELETE;
import static com.silenteight.sens.webapp.user.lock.LockUserUseCaseFixtures.LOCK_COMMAND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LockUserUseCaseTest {

  @Mock
  private UserLocker userLocker;
  @Mock
  private AuditTracer auditTracer;

  private LockUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserLockUseCaseConfiguration().lockUserUseCase(userLocker, auditTracer);
  }

  @Test
  void lockUserCommand_lockUserByUsername() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(LOCK_COMMAND);

    // then
    verify(userLocker).lock(LOCK_COMMAND.getUsername());

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer, times(2)).save(eventCaptor.capture());
    List<AuditEvent> auditEvents = eventCaptor.getAllValues();

    assertAuditEvent(auditEvents.get(0), "UserLockRequested", correlationId);
    assertAuditEvent(auditEvents.get(1), "UserLocked", correlationId);
  }

  private void assertAuditEvent(AuditEvent auditEvent, String type, UUID correlationId) {
    assertThat(auditEvent.getType()).isEqualTo(type);
    assertThat(auditEvent.getEntityAction()).isEqualTo(DELETE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(LOCK_COMMAND);
  }
}
