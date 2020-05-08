package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.UserQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.ADD_ANALYST_ROLE_COMMAND;
import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.OFFSET_DATE_TIME;
import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.USER_DTO;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddRolesToUserUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;
  @Mock
  private UserQuery userQuery;
  @Mock
  private AuditTracer auditTracer;

  private AddRolesToUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserUpdateUseCaseConfiguration()
        .addRolesToUserUseCase(updatedUserRepository, userQuery, auditTracer);

    when(userQuery.find(ADD_ANALYST_ROLE_COMMAND.getUsername())).thenReturn(of(USER_DTO));
  }

  @Test
  void addRoleToUserCommand_updateUser() {
    // given
    UUID correlationId = RequestCorrelation.id();

    // when
    underTest.apply(ADD_ANALYST_ROLE_COMMAND);

    // then
    verify(updatedUserRepository).save(
        updatedUser(
            ADD_ANALYST_ROLE_COMMAND.getUsername(), ADD_ANALYST_ROLE_COMMAND.getRolesToAdd()));

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("UserUpdateRequested");
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(ADD_ANALYST_ROLE_COMMAND);
  }

  private static UpdatedUser updatedUser(String username, Set<String> roles) {
    return UpdatedUser
        .builder()
        .username(username)
        .roles(roles)
        .updateDate(OFFSET_DATE_TIME)
        .build();
  }
}
