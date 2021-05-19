package com.silenteight.sep.usermanagement.keycloak.query.client;

import lombok.NonNull;

import org.keycloak.representations.idm.ClientRepresentation;

public interface ClientQuery {

  ClientRepresentation getByClientId(@NonNull String clientId);
}
