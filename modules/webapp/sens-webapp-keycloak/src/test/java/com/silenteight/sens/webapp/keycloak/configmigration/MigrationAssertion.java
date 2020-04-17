package com.silenteight.sens.webapp.keycloak.configmigration;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.*;

public class MigrationAssertion extends AbstractAssert<MigrationAssertion, Migration> {

  private MigrationAssertion(Migration migration) {
    super(migration, MigrationAssertion.class);
  }

  public static MigrationAssertion assertThatMigration(Migration asserted) {
    return new MigrationAssertion(asserted);
  }

  public MigrationAssertion hasName(String name) {
    assertThat(actual.name()).isEqualTo(name);

    return this;
  }

  public MigrationAssertion hasJson(String json) {
    assertThat(actual.json()).isEqualTo(json);

    return this;
  }
}
