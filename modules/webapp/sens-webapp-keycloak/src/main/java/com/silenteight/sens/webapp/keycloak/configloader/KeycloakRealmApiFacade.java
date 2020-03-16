package com.silenteight.sens.webapp.keycloak.configloader;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToCreateRealmException;
import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToFindRealmException;
import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToPerformPartialImportException;

import io.vavr.control.Try;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.Collection;
import java.util.function.Predicate;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import static java.lang.Boolean.TRUE;

@Slf4j
class KeycloakRealmApiFacade {

  private final RealmsResource realmsResource;

  KeycloakRealmApiFacade(Keycloak keyCloak) {
    this.realmsResource = keyCloak.realms();
  }

  private static final Try<Void> SUCCESS = Try.success(null);

  static Try<Void> performPartialImport(
      RealmResource realmResource,
      PartialImportRepresentation partialImportRepresentation) {
    log.debug("Performing Keycloak partial import for realm");
    return Try.of(() -> realmResource.partialImport(partialImportRepresentation))
        .filter(isOk(), FailedToPerformPartialImportException::new)
        .flatMap(response -> SUCCESS)
        .recoverWith(ClientErrorException.class, FailedToPerformPartialImportException::from);
  }

  private static Predicate<Response> isOk() {
    return response -> response.getStatusInfo().getFamily() == Family.SUCCESSFUL;
  }

  Try<RealmResource> getRealm(String realmName) {
    log.debug("Getting realm {}", realmName);
    return realmExists(realmName)
        .filter(realmExists -> realmExists == TRUE, () -> new FailedToFindRealmException(realmName))
        .map(unused -> realmsResource.realm(realmName))
        .recoverWith(ClientErrorException.class, FailedToFindRealmException::from);
  }

  private Try<Boolean> realmExists(String realmName) {
    log.debug("Checking if realm {} exists", realmName);
    Predicate<RealmRepresentation> sameName = realm -> realm.getRealm().equals(realmName);
    return Try.of(realmsResource::findAll)
        .map(Collection::stream)
        .map(realms -> realms.anyMatch(sameName));
  }

  Try<String> createRealm(RealmRepresentation realmRepresentation) {
    log.debug("Creating realm {}", realmRepresentation.getRealm());
    return Try.run(() -> realmsResource.create(realmRepresentation))
        .map(unused -> realmRepresentation.getRealm())
        .recoverWith(ClientErrorException.class, FailedToCreateRealmException::from);
  }
}
