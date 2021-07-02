package com.silenteight.sep.usermanagement.keycloak.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRoleManagerTest {

  private static final String CLIENT_ID = "id";
  private static final String ROLE_NAME = "TEST_ROLE";

  @InjectMocks
  private ClientRoleManager underTest;
  @Mock
  private RolesResource rolesResource;
  @Mock
  private ClientsResource clientsResource;
  @Mock
  private ClientResource clientResource;
  @Captor
  ArgumentCaptor<RoleRepresentation> roleRepresentationCaptor;

  @Test
  void shouldCreateRoles() {
    // given
    given(clientsResource.get(CLIENT_ID)).willReturn(clientResource);
    given(clientResource.roles()).willReturn(rolesResource);

    // when
    underTest.createRoles(CLIENT_ID, of(ROLE_NAME));

    // then
    verify(rolesResource).create(roleRepresentationCaptor.capture());
    RoleRepresentation captured = roleRepresentationCaptor.getValue();

    assertThat(captured.getClientRole()).isTrue();
    assertThat(captured.getName()).isEqualTo(ROLE_NAME);
  }
}
