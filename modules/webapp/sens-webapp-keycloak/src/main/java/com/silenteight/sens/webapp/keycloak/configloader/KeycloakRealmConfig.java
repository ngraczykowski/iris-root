package com.silenteight.sens.webapp.keycloak.configloader;

import lombok.Value;

import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

@Value
class KeycloakRealmConfig {

  private final RealmRepresentation realmRepresentation;
  private final PartialImportRepresentation partialImportRepresentation;

  String getRealmName() {
    return realmRepresentation.getRealm();
  }
}
