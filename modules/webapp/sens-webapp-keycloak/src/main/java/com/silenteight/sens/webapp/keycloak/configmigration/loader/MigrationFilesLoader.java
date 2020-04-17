package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrationsLoader.CouldNotLoadMigrationsException;

import io.vavr.control.Try;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Set;

@RequiredArgsConstructor
class MigrationFilesLoader {

  private final PathMatchingResourcePatternResolver resourceResolver;
  private final MigrationFileProperties config;

  MigrationFiles load(String path) {
    Set<Resource> migrationResources =
        Try.of(() -> resourceResolver.getResources(path))
            .map(Set::of)
            .recoverWith(problem -> Try.failure(new CouldNotLoadMigrationsException(problem)))
            .get();

    return new MigrationFiles(migrationResources, config);
  }
}
