package com.silenteight.sens.webapp.keycloak.configmigration.loader.template;

public interface KeycloakConfigTemplateParser {

  String parse(String template, KeycloakTemplateProperties properties);

  class CouldNotProcessTemplateException extends RuntimeException {

    private static final long serialVersionUID = -3222953684584150098L;

    public CouldNotProcessTemplateException(Throwable cause) {
      super(cause);
    }
  }
}
