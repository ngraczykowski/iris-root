package com.silenteight.sep.usermanagement.keycloak.query.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;

@RequiredArgsConstructor
class KeycloakClientQuery implements ClientQuery {

  @NotNull
  private final ClientsResource clientsResource;

  @Override
  public ClientRepresentation getByClientId(@NonNull String clientId) {
    return clientsResource
        .findByClientId(clientId)
        .stream()
        .findFirst()
        .orElseThrow(() -> new ClientNotFoundException(clientId));
  }
}
