package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.UserRoles;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.ANALYST;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.AUDITOR;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.USER_1_ROLES;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProviderFixtures.USER_2_NO_ROLES;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SingleRequestRoleProviderTest {

  @InjectMocks
  private SingleRequestRoleProvider underTest;

  @Mock
  private RealmResource realmResource;
  @Mock
  private InternalRoleFilter internalRoleFilter;

  @Test
  void returnsEmptySet_whenUserHasNoRoles() {
    givenUser(USER_2_NO_ROLES);

    List<String> actual = underTest.getForUserId(USER_2_NO_ROLES.getUserId());

    assertThat(actual).isEmpty();
  }

  @Test
  void returnsRoles_whenUserHasRoles() {
    givenUser(USER_1_ROLES);

    List<String> actual = underTest.getForUserId(USER_1_ROLES.getUserId());

    assertThat(actual).containsExactlyInAnyOrder(AUDITOR, ANALYST);
  }

  private void givenUser(UserRoles userRoles) {
    List<RoleRepresentation> rolesRepresentation = getRoleRepresentations(userRoles);

    MappingsRepresentation mappingsRepresentation = mock(MappingsRepresentation.class);
    when(mappingsRepresentation.getRealmMappings()).thenReturn(rolesRepresentation);

    RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
    when(roleMappingResource.getAll()).thenReturn(mappingsRepresentation);

    UserResource userResource = mock(UserResource.class);
    when(userResource.roles()).thenReturn(roleMappingResource);

    UsersResource usersResource = mock(UsersResource.class);
    when(usersResource.get(userRoles.getUserId())).thenReturn(userResource);

    when(realmResource.users()).thenReturn(usersResource);
    if (userRoles.getRoles() != null)
      userRoles.getRoles().forEach(name -> when(internalRoleFilter.test(name)).thenReturn(true));
  }

  @Nullable
  private  List<RoleRepresentation> getRoleRepresentations(UserRoles userRoles) {
    if (userRoles.getRoles() == null)
      return null;

    return userRoles
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
}
