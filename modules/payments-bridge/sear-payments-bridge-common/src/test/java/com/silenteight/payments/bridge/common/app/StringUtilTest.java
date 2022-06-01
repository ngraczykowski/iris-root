/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.payments.bridge.common.app;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringUtilTest {

  @Nested
  class RegexEscapeTest {

    @SuppressWarnings("")
    @Test
    void throwNullPointerExceptionWhenNull() {
      assertThrows(
          NullPointerException.class,
          () -> StringUtil.regexEscape(null));
    }

    @CsvSource({
        "'',''",
        "'  ','  '",
        "'{STRIP}','\\{STRIP\\}'",
        "'STRIP','STRIP'",
        "'50K','50K'",
        "'MTS_TAG','MTS_TAG'",
        "'MTS-TAG','MTS\\-TAG'"
    })
    @ParameterizedTest
    void test(String input, String expectedOutput) {
      assertEquals(expectedOutput, StringUtil.regexEscape(input));
    }
  }

}
