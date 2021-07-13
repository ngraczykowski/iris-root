package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.RolesValidator;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;
import com.silenteight.sep.usermanagement.api.UserRoles;

import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.COUNTRY_GROUPS_SCOPE;
import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.COUNTRY_GROUP_ROLE;
import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.NEW_DISPLAY_NAME_COMMAND;
import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.ROLES_SCOPE;
import static com.silenteight.sens.webapp.user.update.UpdateUserUseCaseFixtures.ROLES;
import static com.silenteight.sens.webapp.user.update.UpdateUserUseCaseFixtures.UPDATED_USER;
import static com.silenteight.sens.webapp.user.update.UpdateUserUseCaseFixtures.UPDATE_USER_COMMAND;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;
  @Mock
  private AuditTracer auditTracer;
  @Mock
  private UserRolesRetriever userRolesRetriever;
  @Mock
  private RolesProperties rolesProperties;

  private UpdateUserUseCase underTest;

  @BeforeEach
  void setUp() {
    given(rolesProperties.getRolesScope()).willReturn(ROLES_SCOPE);
    given(rolesProperties.getCountryGroupsScope()).willReturn(COUNTRY_GROUPS_SCOPE);
    given(rolesProperties.getDefaultCountryGroupRole()).willReturn(COUNTRY_GROUP_ROLE);

    underTest = new UserUpdateUseCaseConfiguration().updateUserUseCase(
        updatedUserRepository,
        new DummyRolesValidator(),
        displayName -> Option.none(),
        auditTracer,
        userRolesRetriever,
        rolesProperties);
  }

  @Test
  void updateDisplayNameCommand_updateUserInRepo() {
    // given
    List<String> roles = of("role1", "role3");
    Map<String, List<String>> scopeRoles = Map.of(
        ROLES_SCOPE, roles, COUNTRY_GROUPS_SCOPE, of("PL"));
    UserRoles userRoles = new ScopeUserRoles(scopeRoles);
    when(userRolesRetriever.rolesOf(NEW_DISPLAY_NAME_COMMAND.getUsername())).thenReturn(userRoles);

    // when
    underTest.apply(UPDATE_USER_COMMAND);

    // then
    verify(updatedUserRepository).save(UPDATED_USER);
  }

  @Test
  void updateDisplayNameCommand_registerEvents() {
    // given
    UUID correlationId = RequestCorrelation.id();
    List<String> roles = of("role1", "role3");
    Map<String, List<String>> scopeRoles = Map.of(ROLES_SCOPE, roles);
    UserRoles userRoles = new ScopeUserRoles(scopeRoles);
    when(userRolesRetriever.rolesOf(NEW_DISPLAY_NAME_COMMAND.getUsername())).thenReturn(userRoles);

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
    assertThat(auditEvent.get(1).getDetails())
        .isEqualTo(updatedUserDetailsWith(ROLES, new HashSet<>(roles)));
  }

  private UpdatedUserDetails updatedUserDetailsWith(
      Set<String> newRoles, Collection<String> currentRoles) {

    return new UpdatedUserDetails(UPDATED_USER, newRoles, currentRoles);
  }

  private static class DummyRolesValidator implements RolesValidator {

    @Override
    public Optional<RolesDontExistError> validate(String scope, Set<String> roles) {
      return Optional.empty();
    }
  }
}
