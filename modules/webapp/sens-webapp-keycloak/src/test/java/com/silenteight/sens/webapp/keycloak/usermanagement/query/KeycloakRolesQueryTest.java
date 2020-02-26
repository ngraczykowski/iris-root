package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import com.silenteight.sens.webapp.user.dto.RolesDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
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

  private static final String AUDITOR = "Auditor";
  private static final String ANALYST = "Analyst";
  private static final String KEYCLOAK_ROLE = "uma_authorization";

  @Mock
  private RolesResource rolesResource;

  private KeycloakRolesQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserQueryConfiguration().keycloakRolesQuery(rolesResource);
  }

  @Test
  void twoWebAppSortedRoles_whenThreeRolesReturnedFromResource() {
    givenThreeRoles();

    RolesDto result = underTest.list();

    assertThat(result.getRoles()).containsExactly(ANALYST, AUDITOR);
  }

  @Test
  void rolesDtoEmpty_whenNoRolesReturnedFromResource() {
    givenNoRoles();

    RolesDto result = underTest.list();

    assertThat(result.getRoles()).isEmpty();
  }

  private void givenThreeRoles() {
    Map<String, List<String>> attributesWithWebAppOrigin = Map.of("origin", of("webapp"));

    RoleRepresentation auditor = roleRepresentation(AUDITOR, attributesWithWebAppOrigin);
    RoleRepresentation analyst = roleRepresentation(ANALYST, attributesWithWebAppOrigin);
    RoleRepresentation keycloakRole = roleRepresentation(KEYCLOAK_ROLE, null);

    when(rolesResource.list()).thenReturn(of(auditor, analyst, keycloakRole));

    RoleResource auditorRoleResource = mock(RoleResource.class);
    when(auditorRoleResource.toRepresentation()).thenReturn(auditor);

    RoleResource analystRoleResource = mock(RoleResource.class);
    when(analystRoleResource.toRepresentation()).thenReturn(analyst);

    RoleResource keycloakRoleResource = mock(RoleResource.class);
    when(keycloakRoleResource.toRepresentation()).thenReturn(keycloakRole);

    when(rolesResource.get(auditor.getName())).thenReturn(auditorRoleResource);
    when(rolesResource.get(analyst.getName())).thenReturn(analystRoleResource);
    when(rolesResource.get(keycloakRole.getName())).thenReturn(keycloakRoleResource);
  }

  private RoleRepresentation roleRepresentation(String name, Map<String, List<String>> attributes) {
    RoleRepresentation auditor = new RoleRepresentation();
    auditor.setName(name);
    auditor.setAttributes(attributes);
    return auditor;
  }

  private void givenNoRoles() {
    when(rolesResource.list()).thenReturn(emptyList());
  }
}
