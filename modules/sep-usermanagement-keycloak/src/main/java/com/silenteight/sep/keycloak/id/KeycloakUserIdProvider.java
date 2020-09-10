package com.silenteight.sep.keycloak.id;

import java.util.Optional;

public interface KeycloakUserIdProvider {

  Optional<String> findId(String username);
}
