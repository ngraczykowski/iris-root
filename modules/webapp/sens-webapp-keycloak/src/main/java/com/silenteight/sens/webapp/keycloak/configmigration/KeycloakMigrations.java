package com.silenteight.sens.webapp.keycloak.configmigration;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Collections.emptyList;

@Value
@Accessors(fluent = true)
public class KeycloakMigrations {

  @Nullable
  Migration baseMigration;

  @NonNull
  List<PartialMigration> partialMigrations;

  public Optional<Migration> baseMigration() {
    return Optional.ofNullable(baseMigration);
  }

  public static KeycloakMigrations empty() {
    return new KeycloakMigrations(null, emptyList());
  }
}
