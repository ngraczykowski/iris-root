package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sep.usermanagement.api.UpdatedUser;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;
import com.silenteight.sep.usermanagement.api.UserQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.*;
import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddRolesToUserUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;
  @Mock
  private UserQuery userQuery;
  @Mock
  private AuditTracer auditTracer;
  @Mock
  private RolesProperties rolesProperties;

  private AddRolesToUserUseCase underTest;

  @BeforeEach
  void setUp() {
    given(rolesProperties.getRolesScope()).willReturn(ROLES_SCOPE);
    given(rolesProperties.getCountryGroupsScope()).willReturn(COUNTRY_GROUPS_SCOPE);

    underTest = new UserUpdateUseCaseConfiguration()
        .addRolesToUserUseCase(updatedUserRepository, userQuery, auditTracer, rolesProperties);

    when(userQuery.find(
        ADD_ANALYST_ROLE_COMMAND.getUsername(), Set.of(ROLES_SCOPE, COUNTRY_GROUPS_SCOPE)))
        .thenReturn(of(USER_DTO));
  }

  @Test
  void addRoleToUserCommand_updateUser() {
    // when
    underTest.apply(ADD_ANALYST_ROLE_COMMAND);

    // then
    verify(updatedUserRepository).save(
        updatedUser(
            ADD_ANALYST_ROLE_COMMAND.getUsername(), ADD_ANALYST_ROLE_COMMAND.getRolesToAdd()));
  }

  private static UpdatedUser updatedUser(String username, Set<String> roles) {
    return UpdatedUser.builder()
        .username(username)
        .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, new ArrayList<>(roles))))
        .updateDate(OFFSET_DATE_TIME)
        .build();
  }
}
