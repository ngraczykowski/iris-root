package com.silenteight.sens.webapp.keycloak.configmigration;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class PartialMigration {

  @NonNull
  String name;

  @NonNull
  String json;

  @NonNull
  MigrationPolicy policy;

  long ordinal;
}
