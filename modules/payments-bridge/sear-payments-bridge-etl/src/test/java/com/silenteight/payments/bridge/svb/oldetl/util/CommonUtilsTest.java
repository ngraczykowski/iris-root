package com.silenteight.payments.bridge.svb.oldetl.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonUtilsTest {

  @ParameterizedTest
  @CsvFileSource(
      resources = "/CommonUtilsGetLastLineNotUsIndexTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parameterizedTest_getLastLineNotUS(
      String linesAsText,
      String applicationCode,
      int expectedLastLineNotUsIndex) {

    List<String> lines = linesAsText.replace("\\n", "\n")
        .lines().collect(Collectors.toList());
    int actualLastNotUsIndex = CommonUtils.getLastLineNotUsIndex(lines, applicationCode);

    assertEquals(expectedLastLineNotUsIndex, actualLastNotUsIndex);
  }
}
