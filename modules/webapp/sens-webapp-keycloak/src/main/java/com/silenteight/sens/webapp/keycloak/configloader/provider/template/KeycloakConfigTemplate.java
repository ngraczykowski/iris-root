package com.silenteight.sens.webapp.keycloak.configloader.provider.template;

import io.vavr.control.Try;

import java.util.Map;

public interface KeycloakConfigTemplate {

  Try<String> process(Map<KeycloakConfigurationKey, Object> values);
}
