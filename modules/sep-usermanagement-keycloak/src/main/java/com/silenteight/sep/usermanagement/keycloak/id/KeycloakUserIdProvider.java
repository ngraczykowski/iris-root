package com.silenteight.sep.usermanagement.keycloak.id;

import java.util.Optional;

public interface KeycloakUserIdProvider {

  Optional<String> findId(String username);
}
