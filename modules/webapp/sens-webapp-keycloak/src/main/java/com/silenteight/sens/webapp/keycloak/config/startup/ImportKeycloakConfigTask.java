package com.silenteight.sens.webapp.keycloak.config.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configloader.KeycloakConfigProvider;
import com.silenteight.sens.webapp.keycloak.configloader.KeycloakHttpConfigLoader;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
class ImportKeycloakConfigTask {

  private final KeycloakHttpConfigLoader keycloakHttpConfigLoader;
  private final KeycloakConfigProvider keycloakConfigProvider;

  @PostConstruct
  public void doImport() {
    log.info("Importing keycloak config");

    keycloakHttpConfigLoader.load(keycloakConfigProvider)
        .getOrElseThrow(CouldNotImportKeycloakConfigOnStartup::new);
  }

  private static class CouldNotImportKeycloakConfigOnStartup extends RuntimeException {

    private static final long serialVersionUID = 3542807187955892326L;

    CouldNotImportKeycloakConfigOnStartup(Throwable cause) {
      super(cause);
    }
  }
}

