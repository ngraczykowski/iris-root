package com.silenteight.sens.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class WebappAuthorityNameNormalizerTest {

  private AuthorityNameNormalizer underTest = new WebappAuthorityNameNormalizer();

  @DisplayName("Maps correctly")
  @ParameterizedTest(name = "\"{0}\" -> \"{1}\"")
  @CsvSource({
      "decision-tree list,DECISION_TREE_LIST",
      "decisions-tree-list,DECISIONS_TREE_LIST",
      "all-roles_VIEW,ALL_ROLES_VIEW",
  })
  void mapsRolesCorrectly(String input, String expected) {
    assertThat(underTest.normalize(input))
        .isEqualTo(expected);
  }
}
