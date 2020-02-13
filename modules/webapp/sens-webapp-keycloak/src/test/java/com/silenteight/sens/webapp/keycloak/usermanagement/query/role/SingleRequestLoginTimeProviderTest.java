package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.ANALYST;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.AUDITOR;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.USER_2_NO_ROLES;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SingleRequestLoginTimeProviderTest {

  static final String LOGIN_EVENT_TYPE = "LOGIN";

  private SingleRequestRoleProvider underTest;

  @Mock
  private RealmResource realmResource;

  @BeforeEach
  void setUp() {
    underTest = new SingleRequestRoleProvider(realmResource);
  }

  @Test
  void returnsEmptySet_whenUserHasNoRoles() {
    givenUser(emptyList());

    List<String> actual = underTest.getForUserId(USER_2_NO_ROLES.getUserId());

    assertThat(actual).isEmpty();
  }

  @Test
  void returnsRoles_whenUserHasRoles() {
    givenUser(asList(AUDITOR, ANALYST));

    List<String> actual = underTest.getForUserId(USER_2_NO_ROLES.getUserId());

    assertThat(actual).containsExactlyInAnyOrder(AUDITOR, ANALYST);
  }


  private void givenUser(List<String> roles) {
    List<RoleRepresentation> rolesRepresentation = roles
        .stream()
        .map(this::getRoleRepresentationMock)
        .collect(toList());

    MappingsRepresentation mappingsRepresentation = mock(MappingsRepresentation.class);
    when(mappingsRepresentation.getRealmMappings()).thenReturn(rolesRepresentation);

    RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
    when(roleMappingResource.getAll()).thenReturn(mappingsRepresentation);

    UserResource userResource = mock(UserResource.class);
    when(userResource.roles()).thenReturn(roleMappingResource);

    UsersResource usersResource = mock(UsersResource.class);
    when(usersResource.get(USER_2_NO_ROLES.getUserId())).thenReturn(userResource);

    when(realmResource.users()).thenReturn(usersResource);
  }

  private RoleRepresentation getRoleRepresentationMock(String role) {
    RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
    when(roleRepresentation.getName()).thenReturn(role);
    return roleRepresentation;
  }
}
