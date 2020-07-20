package com.silenteight.sens.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class WebappNameNormalizerTest {

  private AuthorityNameNormalizer underTest = new WebappRoleNameNormalizer();

  @DisplayName("Maps correctly")
  @ParameterizedTest(name = "\"{0}\" -> \"{1}\"")
  @CsvSource({
      "user manager,ROLE_USER_MANAGER",
      "User-MANAGER,ROLE_USER_MANAGER",
  })
  void mapsRolesCorrectly(String input, String expected) {
    assertThat(underTest.normalize(input))
        .isEqualTo(expected);
  }
}
