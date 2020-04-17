package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.TemplateFilenameChecker;

import java.nio.charset.Charset;

interface MigrationFileProperties {

  String getMigrationsNameSeparator();

  Charset getCharset();

  TemplateFilenameChecker getTemplateFilenameChecker();
}
