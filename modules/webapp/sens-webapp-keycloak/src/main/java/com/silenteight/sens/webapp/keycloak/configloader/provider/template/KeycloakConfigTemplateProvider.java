package com.silenteight.sens.webapp.keycloak.configloader.provider.template;

import io.vavr.control.Try;

public interface KeycloakConfigTemplateProvider {

  Try<KeycloakConfigTemplate> byName(String name);
}
