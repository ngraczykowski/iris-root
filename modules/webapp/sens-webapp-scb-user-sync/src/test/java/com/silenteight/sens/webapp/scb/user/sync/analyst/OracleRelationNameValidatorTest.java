package com.silenteight.sens.webapp.scb.user.sync.analyst;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class OracleRelationNameValidatorTest {

  private OracleRelationNameValidator underTest = new OracleRelationNameValidator();

  @DisplayName("Validates correctly")
  @ParameterizedTest(name = "\"{0}\" -> \"{1}\"")
  @MethodSource("testCases")
  void validatesTableNamesCorrectly(String tableName, boolean result) {
    assertThat(underTest.isValid(tableName, null)).isEqualTo(result);
  }

  private static Stream<Arguments> testCases() {
    return Stream.of(
        Arguments.of("TEST", true),
        Arguments.of("SCHEMA.NAME", true),
        Arguments.of("V$FFF_USERS", true),
        Arguments.of("sens_users", true),
        Arguments.of("SCH$#.V$FFF_USERS", true),
        Arguments.of("PUBLIC_SCHEMA.TABLE", true),
        Arguments.of("A.A", true),
        Arguments.of("schema.relation_name", true),
        Arguments.of(null, false),
        Arguments.of("$V_FFF_USERS", false),
        Arguments.of("A. A", false),
        Arguments.of("A.#", false),
        Arguments.of("#234", false),
        Arguments.of("23234234", false),
        Arguments.of("1234TEST", false),
        Arguments.of(")", false),
        Arguments.of("", false),
        Arguments.of("   ", false),
        Arguments.of("A ", false),
        Arguments.of(" A", false),
        Arguments.of("); DROP DATABASE;", false));
  }
}
