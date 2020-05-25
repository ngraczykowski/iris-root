package com.silenteight.serp.common.support.hibernate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class SilentEightImplicitNamingStrategyResolverTest {

  private SilentEightImplicitNamingStrategyResolver resolver;

  @BeforeEach
  void setUp() {
    resolver = new SilentEightImplicitNamingStrategyResolver("ApplicationName");
  }

  @ParameterizedTest
  @CsvSource({
      "AlertEntity, ApplicationNameAlert",
      "Alert, ApplicationNameAlert",
      "AlertEntityEntity, ApplicationNameAlertEntity"
  })
  void primaryTableNameTest(String original, String expected) {
    assertThat(resolver.resolvePrimaryTableName(original)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "AlertEntity_property, ApplicationNameAlert_property",
      "Alert_property, ApplicationNameAlert_property",
      "AlertEntityEntity_property, ApplicationNameAlertEntity_property"
  })
  void collectionTableNameTest(String original, String expected) {
    assertThat(resolver.resolveCollectionTableName(original)).isEqualTo(expected);
  }
}
