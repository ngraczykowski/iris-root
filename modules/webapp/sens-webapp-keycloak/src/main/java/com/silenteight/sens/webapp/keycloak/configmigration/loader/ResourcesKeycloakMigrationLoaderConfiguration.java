package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakConfigTemplateParser;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
class ResourcesKeycloakMigrationLoaderConfiguration {

  @Bean
  ResourcesKeycloakMigrationsLoader resourcesKeycloakMigrationsLoader(
      KeycloakResourcesMigrationsLoaderProperties config,
      KeycloakConfigTemplateParser parser,
      AuditLog auditLog) {
    return new ResourcesKeycloakMigrationsLoader(
        config, parser, migrationFilesLoader(config), auditLog);
  }

  @NotNull
  private static MigrationFilesLoader migrationFilesLoader(
      KeycloakResourcesMigrationsLoaderProperties config) {
    return new MigrationFilesLoader(new PathMatchingResourcePatternResolver(), config);
  }
}
