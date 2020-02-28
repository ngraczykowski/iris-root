package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.InternalRoleFilter;
import com.silenteight.sens.webapp.user.dto.RolesDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  @Mock
  private RolesResource rolesResource;
  @Mock
  private InternalRoleFilter internalRoleFilter;

  private KeycloakRolesQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserQueryConfiguration()
        .keycloakRolesQuery(rolesResource, internalRoleFilter);
  }

  @Test
  void rolesDtoEmpty_whenNoRolesReturnedFromResource() {
    givenNoRoles();

    RolesDto result = underTest.list();

    assertThat(result.getRoles()).isEmpty();
  }

  @Test
  void twoRoles_whenTwoRolesReturnedFromResource() {
    givenTwoRoles();

    RolesDto result = underTest.list();

    assertThat(result.getRoles()).containsExactly(ANALYST, AUDITOR);
  }

  private void givenNoRoles() {
    when(rolesResource.list()).thenReturn(emptyList());
  }

  private void givenTwoRoles() {
    RoleRepresentation analyst = roleRepresentation(ANALYST);
    RoleRepresentation auditor = roleRepresentation(AUDITOR);
    List<RoleRepresentation> roleRepresentations = of(analyst, auditor);

    when(rolesResource.list()).thenReturn(roleRepresentations);
    when(internalRoleFilter.test(ANALYST)).thenReturn(true);
    when(internalRoleFilter.test(AUDITOR)).thenReturn(true);
  }

  private static RoleRepresentation roleRepresentation(String name) {
    RoleRepresentation auditor = new RoleRepresentation();
    auditor.setName(name);
    auditor.setAttributes(Map.of("origin", of("webapp")));
    return auditor;
  }
}
