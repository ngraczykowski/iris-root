package com.silenteight.sep.usermanagement.keycloak.query.role;

import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.RolesForUser;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientMappingsRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.ANALYST;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.AUDITOR;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.USER_1_ROLES;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.USER_2_NO_ROLES;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SingleRequestRoleProviderTest {

  private static final String ROLE_CLIENT_ID = "frontend";

  @InjectMocks
  private SingleRequestRoleProvider underTest;

  @Mock
  private RealmResource realmResource;

  @Test
  void returnsEmptyListForRoleClient_whenUserHasNoRolesForClient() {
    givenUser(USER_2_NO_ROLES);

    UserRoles actual = underTest.getForUserId(USER_2_NO_ROLES.getUserId(), Set.of(ROLE_CLIENT_ID));

    assertThat(actual.getScopes()).containsExactlyInAnyOrder(ROLE_CLIENT_ID);
    Set<String> roles = actual.getRoles(ROLE_CLIENT_ID);
    assertThat(roles).isEmpty();
  }

  private void givenUser(RolesForUser rolesForUser) {
    Map<String, ClientMappingsRepresentation> rolesMappings =
        Map.of(ROLE_CLIENT_ID, clientRolesMappings(ROLE_CLIENT_ID, rolesForUser));

    MappingsRepresentation mappingsRepresentation = mock(MappingsRepresentation.class);
    when(mappingsRepresentation.getClientMappings()).thenReturn(rolesMappings);

    RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
    when(roleMappingResource.getAll()).thenReturn(mappingsRepresentation);

    UserResource userResource = mock(UserResource.class);
    when(userResource.roles()).thenReturn(roleMappingResource);

    UsersResource usersResource = mock(UsersResource.class);
    when(usersResource.get(rolesForUser.getUserId())).thenReturn(userResource);

    when(realmResource.users()).thenReturn(usersResource);
  }

  private ClientMappingsRepresentation clientRolesMappings(
      String rolesClientId, RolesForUser rolesForUser) {

    ClientMappingsRepresentation clientMappings = new ClientMappingsRepresentation();
    clientMappings.setClient(rolesClientId);
    clientMappings.setMappings(roleRepresentations(rolesForUser));

    return clientMappings;
  }

  @Nullable
  private static List<RoleRepresentation> roleRepresentations(RolesForUser rolesForUser) {
    if (rolesForUser.getRoles() == null)
      return null;

    return rolesForUser
        .getRoles()
        .stream()
        .map(SingleRequestRoleProviderTest::roleRepresentation)
        .collect(toList());
  }

  private static RoleRepresentation roleRepresentation(String name) {
    RoleRepresentation auditor = new RoleRepresentation();
    auditor.setName(name);
    auditor.setAttributes(Map.of("origin", of("webapp")));
    return auditor;
  }

  @Test
  void returnsRoles_whenUserHasRoles() {
    givenUser(USER_1_ROLES);

    UserRoles actual = underTest.getForUserId(USER_1_ROLES.getUserId(), Set.of(ROLE_CLIENT_ID));

    assertThat(actual.getScopes()).containsExactlyInAnyOrder(ROLE_CLIENT_ID);
    Set<String> roles = actual.getRoles(ROLE_CLIENT_ID);
    assertThat(roles).containsExactlyInAnyOrder(AUDITOR, ANALYST);
  }
}
