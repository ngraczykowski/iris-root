package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.UpdatedUser;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;
import com.silenteight.sep.usermanagement.api.UserQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.ADD_ANALYST_ROLE_COMMAND;
import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.OFFSET_DATE_TIME;
import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.USER_DTO;
import static java.util.Optional.of;
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
