package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
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

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;
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
  @Mock
  private UserRolesRetriever userRolesRetriever;

  private UpdateUserDisplayNameUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserUpdateUseCaseConfiguration()
        .updateUserDisplayNameUseCase(updatedUserRepository, auditTracer, userRolesRetriever);
  }

  @Test
  void updateDisplayNameCommand_updateUser() {
    // when
    underTest.apply(NEW_DISPLAY_NAME_COMMAND);

    // then
    UpdatedUser updatedUser = updatedUser(
        NEW_DISPLAY_NAME_COMMAND.getUsername(), NEW_DISPLAY_NAME_COMMAND.getDisplayName());
    verify(updatedUserRepository).save(updatedUser);
  }

  @Test
  void savesAuditEvents() {
    UUID correlationId = RequestCorrelation.id();

    List<String> roles = List.of("role1", "role2");
    when(userRolesRetriever.rolesOf(NEW_DISPLAY_NAME_COMMAND.getUsername())).thenReturn(roles);

    underTest.apply(NEW_DISPLAY_NAME_COMMAND);

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
    assertThat(auditEvent.get(1).getDetails()).isEqualTo(
        new UpdatedUserDetails(
            updatedUser(
                NEW_DISPLAY_NAME_COMMAND.getUsername(), NEW_DISPLAY_NAME_COMMAND.getDisplayName()),
            roles));
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
