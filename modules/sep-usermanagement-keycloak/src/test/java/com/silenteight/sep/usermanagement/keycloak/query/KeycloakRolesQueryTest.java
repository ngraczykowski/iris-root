package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.usermanagement.api.dto.RolesDto;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;
import com.silenteight.sep.usermanagement.keycloak.query.role.InternalRoleFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakRolesQueryTest {

  private static final String CLIENT_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String SCOPE = "frontend";
  private static final String AUDITOR = "Auditor";
  private static final String ANALYST = "Analyst";

  @Mock
  private ClientQuery clientQuery;
  @Mock
  private RoleMappingResource roleMappingResource;
  @Mock
  private InternalRoleFilter internalRoleFilter;

  private KeycloakRolesQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakQueryConfiguration()
        .keycloakRolesQuery(clientQuery, roleMappingResource, internalRoleFilter);
  }

  @Test
  void rolesDtoEmpty_whenNoRolesReturnedFromResource() {
    givenNoRoles();

    RolesDto result = underTest.list(SCOPE);

    assertThat(result.getRoles()).isEmpty();
  }

  private void givenNoRoles() {
    RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
    when(clientQuery.getByClientId(SCOPE)).thenReturn(clientRepresentation());
    when(roleMappingResource.clientLevel(CLIENT_ID)).thenReturn(roleScopeResource);
    when(roleScopeResource.listAll()).thenReturn(emptyList());
  }

  @Test
  void twoRoles_whenTwoRolesReturnedFromResource() {
    givenTwoRoles();

    RolesDto result = underTest.list(SCOPE);

    assertThat(result.getRoles()).containsExactly(ANALYST, AUDITOR);
  }

  private void givenTwoRoles() {
    RoleRepresentation analyst = roleRepresentation(ANALYST);
    RoleRepresentation auditor = roleRepresentation(AUDITOR);
    List<RoleRepresentation> roleRepresentations = of(analyst, auditor);

    RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
    when(clientQuery.getByClientId(SCOPE)).thenReturn(clientRepresentation());
    when(roleMappingResource.clientLevel(CLIENT_ID)).thenReturn(roleScopeResource);
    when(roleScopeResource.listAll()).thenReturn(roleRepresentations);
    when(internalRoleFilter.test(ANALYST)).thenReturn(true);
    when(internalRoleFilter.test(AUDITOR)).thenReturn(true);
  }

  private static ClientRepresentation clientRepresentation() {
    ClientRepresentation client = new ClientRepresentation();
    client.setId(CLIENT_ID);
    client.setClientId(SCOPE);
    return client;
  }

  private static RoleRepresentation roleRepresentation(String name) {
    RoleRepresentation role = new RoleRepresentation();
    role.setName(name);
    role.setAttributes(Map.of("origin", of("webapp")));
    return role;
  }
}
