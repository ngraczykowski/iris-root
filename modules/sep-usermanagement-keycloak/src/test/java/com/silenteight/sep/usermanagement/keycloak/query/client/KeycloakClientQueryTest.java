package com.silenteight.sep.usermanagement.keycloak.query.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakClientQueryTest {

  private static final String ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String CLIENT_ID = "frontend";
  private static final String NAME = "Frontend";

  @InjectMocks
  private KeycloakClientQuery underTest;

  @Mock
  private ClientsResource clientsResource;

  @Test
  void shouldGetClientByClientId() {
    // given
    given(clientsResource.findByClientId(CLIENT_ID)).willReturn(of(makeClientRepresentation()));

    // when
    ClientRepresentation client = underTest.getByClientId(CLIENT_ID);

    // then
    assertThat(client.getId()).isEqualTo(ID);
    assertThat(client.getClientId()).isEqualTo(CLIENT_ID);
    assertThat(client.getName()).isEqualTo(NAME);
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
    client.setName(NAME);

    return client;
  }
}
