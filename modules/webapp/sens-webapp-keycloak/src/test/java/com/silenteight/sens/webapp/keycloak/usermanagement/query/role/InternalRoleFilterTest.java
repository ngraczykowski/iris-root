package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternalRoleFilterTest {

  private static final String ANALYST = "Analyst";
  private static final String KEYCLOAK_ROLE = "uma_authorization";

  @InjectMocks
  private InternalRoleFilter underTest;

  @Mock
  private RolesResource rolesResource;

  @Test
  void isNotInternalRole_whenRoleNameWithoutAttributesGiven() {
    // given
    givenRoleRepresentation(KEYCLOAK_ROLE, null);

    // when
    boolean result = underTest.test(KEYCLOAK_ROLE);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void isInternalRole_whenRoleNameWithAttributesGiven() {
    // given
    Map<String, List<String>> attributesWithWebAppOrigin = Map.of("origin", of("webapp"));
    givenRoleRepresentation(ANALYST, attributesWithWebAppOrigin);

    // when
    boolean result = underTest.test(ANALYST);

    // then
    assertThat(result).isTrue();
  }

  private void givenRoleRepresentation(String name, Map<String, List<String>> attributes) {
    RoleRepresentation roleRepresentation = new RoleRepresentation();
    roleRepresentation.setName(name);
    roleRepresentation.setAttributes(attributes);

    RoleResource roleResource = mock(RoleResource.class);
    when(roleResource.toRepresentation()).thenReturn(roleRepresentation);

    when(rolesResource.get(name)).thenReturn(roleResource);
  }
}
