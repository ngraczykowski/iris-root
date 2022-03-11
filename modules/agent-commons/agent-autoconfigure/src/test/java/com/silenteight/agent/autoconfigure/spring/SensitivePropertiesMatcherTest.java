package com.silenteight.agent.autoconfigure.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class SensitivePropertiesMatcherTest {

  private SensitivePropertiesMatcher matcher;

  @BeforeEach
  void setUp() {
    matcher = new SensitivePropertiesMatcher();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "serp.config.secret",
      "serp.db.password",
      "serp.keystore.password",
      "spring.cloud.consul.tls.certificate-password",
      "serp.db.password1",
      "serp.db.password2",
      "serp.db.pass",
      "serp.db.pass2",
      "serp.pass.db",
      "serp.db.key",
      "DB_PASS_001",
      "DB_PASSWORD_001",
      "DB_PASS1",
      "DB_PASSW",
      "DB_PASSWORD1",
      "PASSWORD",
      "PASSW1",
      "DBPASS",
      "SOME_PASSDB"
  })
  void matchTest(String prop) {
    assertThat(matcher.matches(prop)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "   ",
      "P1A1S1S1W1O1R1D",
      "some.properties",
      "some.keyword",
      "some.credit.value"
  })
  void mismatchTest(String prop) {
    assertThat(matcher.matches(prop)).isFalse();
  }
}
