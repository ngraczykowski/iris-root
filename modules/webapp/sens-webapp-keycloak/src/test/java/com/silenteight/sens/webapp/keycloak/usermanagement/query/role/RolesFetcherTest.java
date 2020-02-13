package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.ANALYST;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.AUDITOR;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.USER_1_ROLES;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesFetcherTest {

  @Mock
  private RolesResource rolesResource;

  private RolesFetcher underTest;

  @BeforeEach
  void setUp() {
    underTest = new RolesFetcher(rolesResource);
  }

  @Test
  void emptyMap_noUsersFetching() {
    givenNoUsers();

    Map<String, @NonNull List<String>> actual = underTest.fetch();

    assertThat(actual).isEmpty();
  }

  @Test
  void updateAllUsers_whenUsersReturnedByResource() {
    givenUsers();

    Map<String, @NonNull List<String>> actual = underTest.fetch();

    assertThat(actual).containsExactly(
        entry(USER_1_ROLES.getUserId(), USER_1_ROLES.getRoles()));
  }

  private void givenNoUsers() {
    when(rolesResource.list()).thenReturn(emptyList());
  }

  private void givenUsers() {
    UserRepresentation user1 = mock(UserRepresentation.class);
    when(user1.getId()).thenReturn(USER_1_ROLES.getUserId());

    RoleResource roleAnalyst = mock(RoleResource.class);
    when(rolesResource.get(ANALYST)).thenReturn(roleAnalyst);
    when(roleAnalyst.getRoleUserMembers()).thenReturn(singleton(user1));

    RoleResource roleAuditor = mock(RoleResource.class);
    when(rolesResource.get(AUDITOR)).thenReturn(roleAuditor);
    when(roleAuditor.getRoleUserMembers()).thenReturn(singleton(user1));

    RoleRepresentation analystRole = mock(RoleRepresentation.class);
    when(analystRole.getName()).thenReturn(ANALYST);
    RoleRepresentation auditorRole = mock(RoleRepresentation.class);
    when(auditorRole.getName()).thenReturn(AUDITOR);

    when(rolesResource.list()).thenReturn(asList(analystRole, auditorRole));
  }
}
