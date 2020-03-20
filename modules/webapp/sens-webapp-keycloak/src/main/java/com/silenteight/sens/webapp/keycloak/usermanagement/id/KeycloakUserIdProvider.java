package com.silenteight.sens.webapp.keycloak.usermanagement.id;

import java.util.Optional;

public interface KeycloakUserIdProvider {

  Optional<String> findId(String username);
}
