package com.silenteight.sep.usermanagement.keycloak.query.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakClientQueryTest {

  private static final String ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String CLIENT_ID = "frontend";
  private static final String CLIENT_NAME = "Frontend";
  private static final String ROLE_NAME = "TEST_ROLE";

  @InjectMocks
  private KeycloakClientQuery underTest;

  @Mock
  private ClientsResource clientsResource;

  @Mock
  private RealmResource realmResource;

  @Mock
  private ClientResource clientResource;

  @Mock
  private RolesResource rolesResource;

  @Test
  void shouldGetClientByClientId() {
    // given
    given(clientsResource.findByClientId(CLIENT_ID)).willReturn(of(makeClientRepresentation()));

    // when
    ClientRepresentation client = underTest.getByClientId(CLIENT_ID);

    // then
    assertThat(client.getId()).isEqualTo(ID);
    assertThat(client.getClientId()).isEqualTo(CLIENT_ID);
    assertThat(client.getName()).isEqualTo(CLIENT_NAME);
  }

  @Test
  void shouldGetRolesByClientId() {
    // given
    given(realmResource.clients()).willReturn(clientsResource);
    given(clientsResource.get(CLIENT_ID)).willReturn(clientResource);
    given(clientResource.roles()).willReturn(rolesResource);
    given(rolesResource.list()).willReturn(of(makeRoleRepresentation()));

    // when
    Set<String> roles = underTest.getRoles(CLIENT_ID);

    // then
    assertThat(roles).isNotEmpty();
    String actual = roles.stream().findFirst().get();
    assertThat(actual).isEqualTo(ROLE_NAME);
  }

  @Test
  void shouldThrowIfClientNotFound() {
    given(clientsResource.findByClientId(CLIENT_ID)).willReturn(emptyList());

    assertThatThrownBy(() -> underTest.getByClientId(CLIENT_ID))
        .isInstanceOf(ClientNotFoundException.class)
        .hasMessageContaining("clientId");
  }

  private static ClientRepresentation makeClientRepresentation() {
    ClientRepresentation client = new ClientRepresentation();
    client.setId(ID);
    client.setClientId(CLIENT_ID);
    client.setName(CLIENT_NAME);

    return client;
  }

  private static RoleRepresentation makeRoleRepresentation() {
    RoleRepresentation role = new RoleRepresentation();
    role.setName(ROLE_NAME);
    return role;
  }
}
