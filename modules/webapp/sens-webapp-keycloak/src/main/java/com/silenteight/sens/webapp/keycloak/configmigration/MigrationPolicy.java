package com.silenteight.sens.webapp.keycloak.configmigration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Stream.of;

@RequiredArgsConstructor
@Getter
public enum MigrationPolicy {
  ON_EVERY_RUN("onEveryRun"),
  ON_FIRST_RUN("onFirstRun");

  private final String name;

  public static MigrationPolicy fromName(String name) {
    return of(values())
        .filter(migrationPolicy -> migrationPolicy.getName().equals(name))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Could not find policy given by name " + name));
  }
}
