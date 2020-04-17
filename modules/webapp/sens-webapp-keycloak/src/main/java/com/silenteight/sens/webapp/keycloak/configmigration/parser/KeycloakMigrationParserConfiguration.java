package com.silenteight.sens.webapp.keycloak.configmigration.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakMigrationParserConfiguration {

  @Bean
  KeycloakMigrationsParser keycloakMigrationParser(ObjectMapper objectMapper) {
    return new KeycloakMigrationsParser(objectMapper);
  }
}
