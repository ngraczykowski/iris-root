package com.silenteight.sens.webapp.keycloak.configmigration;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.*;

public class PartialMigrationAssertion
    extends AbstractAssert<PartialMigrationAssertion, PartialMigration> {

  private PartialMigrationAssertion(PartialMigration partialMigration) {
    super(partialMigration, PartialMigrationAssertion.class);
  }

  public static PartialMigrationAssertion assertThatPartialMigration(
      PartialMigration partialMigration) {
    return new PartialMigrationAssertion(partialMigration);
  }

  public PartialMigrationAssertion hasName(String name) {
    assertThat(actual.name()).isEqualTo(name);

    return this;
  }

  public PartialMigrationAssertion hasJson(String json) {
    assertThat(actual.json()).isEqualTo(json);

    return this;
  }

  public PartialMigrationAssertion hasPolicy(MigrationPolicy policy) {
    assertThat(actual.policy()).isEqualTo(policy);

    return this;
  }

  public PartialMigrationAssertion hasOrdinal(long ordinal) {
    assertThat(actual.ordinal()).isEqualTo(ordinal);

    return this;
  }
}
