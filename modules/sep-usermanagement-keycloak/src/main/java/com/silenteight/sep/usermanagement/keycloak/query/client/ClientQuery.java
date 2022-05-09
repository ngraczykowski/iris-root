package com.silenteight.sep.usermanagement.keycloak.query.client;

import lombok.NonNull;

import org.keycloak.representations.idm.ClientRepresentation;

import java.util.Set;

public interface ClientQuery {

  ClientRepresentation getByClientId(@NonNull String clientId);

  Set<String> getRoles(@NonNull String clientId);
}
