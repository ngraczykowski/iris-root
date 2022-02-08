package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakRolesQueryTest {

  private static final String CLIENT_ID = randomUUID().toString();
  private static final String SCOPE = "frontend";
  private static final String AUDITOR = "Auditor";
  private static final String ANALYST = "Analyst";

  @Mock
  ClientQuery clientQuery;
  @Mock
  private ClientsResource clientsResource;
  @Mock
  private ClientRepresentation clientRepresentation;
  @Mock
  private ClientResource clientResource;
  @Mock
  private RolesResource rolesResource;

  private KeycloakRolesQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakQueryConfiguration()
        .keycloakRolesQuery(clientQuery, clientsResource);
  }

  @Test
  void rolesDtoEmpty_whenNoRolesReturnedFromResource() {
    givenNoRoles();

    RolesDto result = underTest.list(SCOPE);

    assertThat(result.getRoles()).isEmpty();
  }

  private void givenNoRoles() {
    when(clientQuery.getByClientId(SCOPE)).thenReturn(clientRepresentation);
    when(clientRepresentation.getId()).thenReturn(CLIENT_ID);
    when(clientsResource.get(CLIENT_ID)).thenReturn(clientResource);
    when(clientResource.roles()).thenReturn(rolesResource);
    when(rolesResource.list()).thenReturn(emptyList());
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

    when(clientQuery.getByClientId(SCOPE)).thenReturn(clientRepresentation);
    when(clientRepresentation.getId()).thenReturn(CLIENT_ID);
    when(clientsResource.get(CLIENT_ID)).thenReturn(clientResource);
    when(clientResource.roles()).thenReturn(rolesResource);
    when(rolesResource.list()).thenReturn(roleRepresentations);
  }

  private static RoleRepresentation roleRepresentation(String name) {
    RoleRepresentation role = new RoleRepresentation();
    role.setName(name);
    role.setAttributes(Map.of("origin", of("webapp")));
    return role;
  }
}
