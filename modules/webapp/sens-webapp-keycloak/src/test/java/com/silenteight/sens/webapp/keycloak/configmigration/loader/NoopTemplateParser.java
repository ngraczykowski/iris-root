package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakConfigTemplateParser;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakTemplateProperties;

class NoopTemplateParser implements KeycloakConfigTemplateParser {

  @Override
  public String parse(String template, KeycloakTemplateProperties properties)
      throws CouldNotProcessTemplateException {
    return template;
  }
}
