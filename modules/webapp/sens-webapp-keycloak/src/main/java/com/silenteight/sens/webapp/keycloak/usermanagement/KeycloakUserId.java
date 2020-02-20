package com.silenteight.sens.webapp.keycloak.usermanagement;

import lombok.Value;

@Value(staticConstructor = "of")
public class KeycloakUserId {

  String userId;
}
