package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.UpdatedUser;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
    UpdatedUser updatedUser = updatedUser(
        NEW_DISPLAY_NAME_COMMAND.getUsername(), NEW_DISPLAY_NAME_COMMAND.getDisplayName());
    verify(updatedUserRepository).save(updatedUser);

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer, times(2)).save(eventCaptor.capture());
    List<AuditEvent> auditEvent = eventCaptor.getAllValues();

    assertThat(auditEvent.get(0).getType()).isEqualTo("UserUpdateRequested");
    assertThat(auditEvent.get(0).getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.get(0).getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.get(0).getDetails()).isEqualTo(NEW_DISPLAY_NAME_COMMAND);

    assertThat(auditEvent.get(1).getType()).isEqualTo("UserUpdated");
    assertThat(auditEvent.get(1).getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.get(1).getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.get(1).getDetails()).isEqualTo(updatedUser);
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
