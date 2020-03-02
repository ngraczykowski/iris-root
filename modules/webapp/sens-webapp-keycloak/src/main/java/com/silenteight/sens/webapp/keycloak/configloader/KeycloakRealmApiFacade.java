package com.silenteight.sens.webapp.keycloak.configloader;

import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToCreateRealmException;
import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToFindRealmException;
import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToPerformPartialImportException;

import io.vavr.control.Try;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;

class KeycloakRealmApiFacade {

  private final RealmsResource realmsResource;

  KeycloakRealmApiFacade(Keycloak keyCloak) {
    this.realmsResource = keyCloak.realms();
  }

  static Try<Void> performPartialImport(
      RealmResource realmResource,
      PartialImportRepresentation partialImportRepresentation) {
    return Try.run(() -> realmResource.partialImport(partialImportRepresentation))
        .recoverWith(ClientErrorException.class, FailedToPerformPartialImportException::from);
  }

  Try<RealmResource> getRealm(String realmName) {
    return Try.of(() -> realmsResource.realm(realmName))
        .recoverWith(NotFoundException.class, FailedToFindRealmException::from);
  }

  Try<String> createRealm(RealmRepresentation realmRepresentation) {
    return Try.run(() -> realmsResource.create(realmRepresentation))
        .map(unused -> realmRepresentation.getDisplayName())
        .recoverWith(ClientErrorException.class, FailedToCreateRealmException::from);
  }
}
