package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakTemplateProperties;

public interface KeycloakResourcesMigrationsLoaderProperties extends MigrationFileProperties {

  String getMigrationsPath();

  KeycloakTemplateProperties getConfigTemplateProperties();
}
