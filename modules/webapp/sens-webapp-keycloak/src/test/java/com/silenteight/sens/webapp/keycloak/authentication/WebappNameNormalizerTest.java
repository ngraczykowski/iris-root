package com.silenteight.sens.webapp.keycloak.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class WebappNameNormalizerTest {

  private AuthorityNameNormalizer underTest = new WebappRoleNameNormalizer();

  @DisplayName("Maps correctly")
  @ParameterizedTest(name = "\"{0}\" -> \"{1}\"")
  @CsvSource({
      "Decision tree-manager,ROLE_DECISION_TREE_MANAGER",
      "Decision_tree_manager,ROLE_DECISION_TREE_MANAGER",
      "Decision tree manager,ROLE_DECISION_TREE_MANAGER",
      "user manager,ROLE_USER_MANAGER",
      "User-MANAGER,ROLE_USER_MANAGER",
  })
  void mapsRolesCorrectly(String input, String expected) {
    assertThat(underTest.normalize(input))
        .isEqualTo(expected);
  }
}
