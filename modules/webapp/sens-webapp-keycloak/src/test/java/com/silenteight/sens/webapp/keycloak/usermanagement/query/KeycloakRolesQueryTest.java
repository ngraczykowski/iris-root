package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import com.silenteight.sens.webapp.user.dto.RolesDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

  private KeycloakRolesQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserQueryConfiguration().keycloakRolesQuery(rolesResource);
  }

  @Test
  void rolesDtoWith2SortedRoles_whenTwoRolesReturnedFromResource() {
    givenTwoRoles();

    RolesDto result = underTest.list();

    assertThat(result.getRoles()).containsExactly(ANALYST, AUDITOR);
  }

  @Test
  void rolesDtoEmpty_whenNoRolesReturnedFromResource() {
    givenNoRoles();

    RolesDto result = underTest.list();

    assertThat(result.getRoles()).isEmpty();
  }

  private void givenTwoRoles() {
    RoleRepresentation role1 = mock(RoleRepresentation.class);
    when(role1.getName()).thenReturn(AUDITOR);

    RoleRepresentation role2 = mock(RoleRepresentation.class);
    when(role2.getName()).thenReturn(ANALYST);

    when(rolesResource.list()).thenReturn(of(role1, role2));
  }

  private void givenNoRoles() {
    when(rolesResource.list()).thenReturn(emptyList());
  }
}
