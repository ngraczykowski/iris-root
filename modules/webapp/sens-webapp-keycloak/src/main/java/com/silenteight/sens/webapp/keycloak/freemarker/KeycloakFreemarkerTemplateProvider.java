package com.silenteight.sens.webapp.keycloak.freemarker;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigTemplate;
import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigTemplateProvider;
import com.silenteight.sens.webapp.keycloak.freemarker.exception.CouldNotLoadTemplateException;

import freemarker.template.Configuration;
import io.vavr.control.Try;

import java.io.IOException;

@RequiredArgsConstructor
public class KeycloakFreemarkerTemplateProvider implements KeycloakConfigTemplateProvider {

  private final Configuration freemarker;

  public Try<KeycloakConfigTemplate> byName(String name) {
    Try<FreemarkerKeycloakConfigTemplate> keycloakConfigTemplate =
        Try.of(() -> freemarker.getTemplate(name))
            .map(FreemarkerKeycloakConfigTemplate::new)
            .recoverWith(IOException.class, CouldNotLoadTemplateException::from);

    return Try.narrow(keycloakConfigTemplate);
  }
}
