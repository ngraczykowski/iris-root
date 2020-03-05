package com.silenteight.sens.webapp.keycloak.configloader.provider.template;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.configloader.KeycloakConfigProvider;

@RequiredArgsConstructor
public class TemplateKeycloakConfigProvider implements KeycloakConfigProvider {

  private final String configTemplateName;
  private final KeycloakConfigTemplateProvider keycloakConfigTemplateProvider;
  private final KeycloakTemplateConfigValues keycloakTemplateConfigValues;

  @Override
  public String json() {
    return keycloakConfigTemplateProvider.byName(configTemplateName)
        .flatMap(keycloakConfigTemplate -> keycloakConfigTemplate.process(
            keycloakTemplateConfigValues.getValueByConfigurationKey()))
        .get();
  }
}

