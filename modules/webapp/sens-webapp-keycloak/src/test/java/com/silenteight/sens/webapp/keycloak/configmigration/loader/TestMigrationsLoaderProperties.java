package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import lombok.Builder;
import lombok.Value;

import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakTemplateProperties;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.TemplateFilenameChecker;

import java.nio.charset.Charset;

@Value
@Builder(builderClassName = "Builder")
class TestMigrationsLoaderProperties implements KeycloakResourcesMigrationsLoaderProperties {

  private final String migrationsPath;
  private final KeycloakTemplateProperties configTemplateProperties;
  private final String migrationsNameSeparator;
  private final Charset charset;
  private final TemplateFilenameChecker templateFilenameChecker;
}
