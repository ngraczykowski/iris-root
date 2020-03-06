package com.silenteight.sens.webapp.keycloak.configloader;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToFindRealmException;

import io.vavr.control.Try;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.PartialImportRepresentation.Policy;

@Slf4j
@RequiredArgsConstructor
public class KeycloakHttpConfigLoader {

  private final Policy importPolicy;
  private final KeycloakRealmApiFacade keycloak;
  private final KeycloakConfigParser keycloakConfigParser;

  public Try<Void> load(KeycloakConfigProvider keycloakConfigProvider) {
    log.info("Importing Keycloak config through HTTP with policy {}", importPolicy);

    return keycloakConfigParser.processJson(keycloakConfigProvider.json())
        .flatMap(this::getOrCreateRealm)
        .flatMap(this::performPartialImport);
  }

  private Try<RealmWithConfig> getOrCreateRealm(KeycloakRealmConfig keycloakRealmConfig) {
    return keycloak.getRealm(keycloakRealmConfig.getRealmName())
        .recoverWith(FailedToFindRealmException.class, e -> createAndGetRealm(keycloakRealmConfig))
        .map(realmResource -> new RealmWithConfig(realmResource, keycloakRealmConfig));
  }

  private Try<RealmResource> createAndGetRealm(KeycloakRealmConfig keycloakRealmConfig) {
    return keycloak.createRealm(keycloakRealmConfig.getRealmRepresentation())
        .flatMap(keycloak::getRealm);
  }

  private Try<Void> performPartialImport(RealmWithConfig realmWithConfig) {
    PartialImportRepresentation partialRepresentation = realmWithConfig.getPartialRepresentation();
    partialRepresentation.setIfResourceExists(importPolicy.name());

    return KeycloakRealmApiFacade.performPartialImport(
        realmWithConfig.getRealmsResource(), partialRepresentation);
  }

  @Value
  private static class RealmWithConfig {

    RealmResource realmsResource;
    KeycloakRealmConfig config;

    PartialImportRepresentation getPartialRepresentation() {
      return config.getPartialImportRepresentation();
    }
  }
}
