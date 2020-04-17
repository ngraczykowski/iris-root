package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import lombok.ToString;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static one.util.streamex.StreamEx.of;

@ToString
class MigrationFiles {

  private final List<MigrationFile> migrationFilesSortedByOrdinal;
  private final MigrationFileProperties properties;

  MigrationFiles(Set<Resource> migrationResource, MigrationFileProperties properties) {
    this.properties = properties;
    this.migrationFilesSortedByOrdinal = sortedByOrdinal(migrationResource);
  }

  @NotNull
  private List<MigrationFile> sortedByOrdinal(Set<Resource> migrationResource) {
    return migrationResource
        .stream()
        .map(this::createMigrationFile)
        .sorted(comparing(MigrationFile::getOrder))
        .collect(toList());
  }

  MigrationFile createMigrationFile(Resource resource) {
    return new MigrationFile(resource, properties);
  }

  Optional<MigrationFile> getBaseMigration() {
    return migrationFilesSortedByOrdinal.stream().findFirst();
  }

  List<MigrationFile> getPartialMigrations() {
    return of(migrationFilesSortedByOrdinal).skip(1).toList();
  }

  boolean empty() {
    return getPartialMigrations().isEmpty() && getBaseMigration().isEmpty();
  }
}
