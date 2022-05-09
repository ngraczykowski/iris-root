package com.silenteight.sep.usermanagement.keycloak;

import lombok.Value;

@Value(staticConstructor = "of")
public class KeycloakUserId {

  String userId;
}
