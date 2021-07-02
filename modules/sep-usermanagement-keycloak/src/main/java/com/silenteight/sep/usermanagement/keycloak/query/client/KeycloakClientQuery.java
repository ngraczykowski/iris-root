package com.silenteight.sep.usermanagement.keycloak.query.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class KeycloakClientQuery implements ClientQuery {

  @NotNull
  private final ClientsResource clientsResource;

  @NotNull
  private final RealmResource realmResource;

  @Override
  public ClientRepresentation getByClientId(@NonNull String clientId) {
    return clientsResource
        .findByClientId(clientId)
        .stream()
        .findFirst()
        .orElseThrow(() -> new ClientNotFoundException(clientId));
  }

  @Override
  public Set<String> getRoles(@NonNull String clientId) {
    return realmResource
        .clients()
        .get(clientId)
        .roles()
        .list()
        .stream()
        .map(RoleRepresentation::getName)
        .collect(Collectors.toSet());
  }
}
