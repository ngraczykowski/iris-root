package com.silenteight.sep.keycloak;

import lombok.Value;

@Value(staticConstructor = "of")
public class KeycloakUserId {

  String userId;
}
