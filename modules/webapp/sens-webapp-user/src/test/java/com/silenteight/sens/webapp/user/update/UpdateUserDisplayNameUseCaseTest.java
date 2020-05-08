package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.NEW_DISPLAY_NAME_COMMAND;
import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.OFFSET_DATE_TIME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserDisplayNameUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;
  @Mock
  private AuditTracer auditTracer;

  private UpdateUserDisplayNameUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserUpdateUseCaseConfiguration()
        .updateUserDisplayNameUseCase(updatedUserRepository, auditTracer);
  }

  @Test
  void updateDisplayNameCommand_updateUser() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(NEW_DISPLAY_NAME_COMMAND);

    // then
    verify(updatedUserRepository).save(
        updatedUser(
            NEW_DISPLAY_NAME_COMMAND.getUsername(), NEW_DISPLAY_NAME_COMMAND.getDisplayName()));

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("UserUpdateRequested");
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(NEW_DISPLAY_NAME_COMMAND);
  }

  private static UpdatedUser updatedUser(String username, String displayName) {
    return UpdatedUser
        .builder()
        .username(username)
        .displayName(displayName)
        .updateDate(OFFSET_DATE_TIME)
        .build();
  }
}
