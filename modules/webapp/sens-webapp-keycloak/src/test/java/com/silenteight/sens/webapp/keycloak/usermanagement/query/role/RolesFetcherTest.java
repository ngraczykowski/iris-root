package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.ANALYST;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.AUDITOR;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.USER_1_ROLES;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesFetcherTest {

  @InjectMocks
  private RolesFetcher underTest;

  @Mock
  private RolesResource rolesResource;
  @Mock
  private InternalRoleFilter internalRoleFilter;

  private final Fixtures fixtures = new Fixtures();

  @Test
  void emptyMap_whenNoUsersFetching() {
    when(rolesResource.list()).thenReturn(emptyList());

    Map<String, @NonNull List<String>> actual = underTest.fetch();

    assertThat(actual).isEmpty();
  }

  @Test
  void userWithRoles_whenNoneRoleWereFiltered() {
    // given
    when(rolesResource.list()).thenReturn(asList(fixtures.analystRole, fixtures.auditorRole));

    when(internalRoleFilter.test(ANALYST)).thenReturn(true);
    when(internalRoleFilter.test(AUDITOR)).thenReturn(true);

    RoleResource roleAnalyst = mock(RoleResource.class);
    when(rolesResource.get(ANALYST)).thenReturn(roleAnalyst);
    when(roleAnalyst.getRoleUserMembers()).thenReturn(singleton(fixtures.user));

    RoleResource roleAuditor = mock(RoleResource.class);
    when(rolesResource.get(AUDITOR)).thenReturn(roleAuditor);
    when(roleAuditor.getRoleUserMembers()).thenReturn(singleton(fixtures.user));

    // when
    Map<String, @NonNull List<String>> actual = underTest.fetch();

    // then
    assertThat(actual).containsExactly(
        entry(USER_1_ROLES.getUserId(), List.of(ANALYST, AUDITOR)));
  }

  @Test
  void userWithRole_whenAuditorRoleWasFiltered() {
    // given
    when(rolesResource.list()).thenReturn(asList(fixtures.analystRole, fixtures.auditorRole));

    when(internalRoleFilter.test(ANALYST)).thenReturn(true);
    when(internalRoleFilter.test(AUDITOR)).thenReturn(false);

    RoleResource roleAnalyst = mock(RoleResource.class);
    when(rolesResource.get(ANALYST)).thenReturn(roleAnalyst);
    when(roleAnalyst.getRoleUserMembers()).thenReturn(singleton(fixtures.user));

    // when
    Map<String, @NonNull List<String>> actual = underTest.fetch();

    // then
    assertThat(actual).containsExactly(
        entry(USER_1_ROLES.getUserId(), List.of(ANALYST)));
  }

  private static class Fixtures {

    UserRepresentation user = prepareUserRepresentation();

    RoleRepresentation analystRole = prepareRoleRepresentation(ANALYST);
    RoleRepresentation auditorRole = prepareRoleRepresentation(AUDITOR);

    private UserRepresentation prepareUserRepresentation() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setId(USER_1_ROLES.getUserId());
      return userRepresentation;
    }

    private RoleRepresentation prepareRoleRepresentation(String name) {
      RoleRepresentation roleRepresentation = new RoleRepresentation();
      roleRepresentation.setName(name);
      return roleRepresentation;
    }
  }
}
