package com.silenteight.sens.webapp.keycloak.configloader.provider.template;

import lombok.Value;

import java.util.Map;

@Value
public class KeycloakTemplateConfigValues {

  Map<KeycloakConfigurationKey, String> valueByConfigurationKey;
}
