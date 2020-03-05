package com.silenteight.sens.webapp.keycloak.configloader.provider.template;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
class TemplateKeycloakConfigProviderConfiguration {

  // TODO(bgulowaty): Wire up (WA-360)
  TemplateKeycloakConfigProvider productionKeycloakConfigProvider(
      @Qualifier("keycloakConfigTemplateName") String templateName,
      KeycloakConfigTemplateProvider templateProvider,
      KeycloakTemplateConfigValues values) {

    return new TemplateKeycloakConfigProvider(templateName, templateProvider, values);
  }
}
