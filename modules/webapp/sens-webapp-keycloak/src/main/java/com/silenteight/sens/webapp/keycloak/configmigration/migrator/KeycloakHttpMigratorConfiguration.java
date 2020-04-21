package com.silenteight.sens.webapp.keycloak.configmigration.migrator;

import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
class KeycloakHttpMigratorConfiguration {

  @Bean
  KeycloakHttpMigrator keycloakMigrator(
      KeycloakHttpBodiesParser keycloakHttpBodiesParser,
      RealmNameExtractor realmNameExtractor,
      KeycloakRealmMigrationApi migrationApi,
      AuditLog auditLog
  ) {
    return new KeycloakHttpMigrator(
        migrationApi,
        keycloakHttpBodiesParser,
        realmNameExtractor,
        auditLog
    );
  }
}
