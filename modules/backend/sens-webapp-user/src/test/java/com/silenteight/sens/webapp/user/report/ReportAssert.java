package com.silenteight.sens.webapp.user.report;

import com.silenteight.sens.webapp.report.Report;

import org.assertj.core.api.AbstractAssert;

class ReportAssert extends AbstractAssert<ReportAssert, Report> {

  private static final String EMPTY_LINE = "";
  private static final String INVALID_NAME = "Invalid name.\n";
  private static final String INVALID_HEADER = "Invalid header.\n";
  private static final String INVALID_DATA = "Invalid data at line %s.\n";
  private static final String INVALID_LINE_NUMBER =
      "Line number %d is not available (index starts at 0). Number of data lines in the report: %d";
  private static final String NOT_EQUAL_MSG_TEMPLATE =
      "Expecting:\n<\"%s\">\nto be equal to:\n<\"%s\">\nbut was not";

  ReportAssert(Report report) {
    super(report, ReportAssert.class);
  }

  static ReportAssert assertThat(Report report) {
    return new ReportAssert(report);
  }

  ReportAssert hasName(String expectedName) {
    isNotNull();

    String actualName = actual.getReportFileName();

    if (!actualName.equals(expectedName)) {
      failWithMessage(INVALID_NAME + NOT_EQUAL_MSG_TEMPLATE, expectedName, actualName);
    }

    return this;
  }

  ReportAssert hasFields(String... fields) {
    isNotNull();

    String expectedLine = asCsvLine(fields);
    String actualLine = actual.getReportContent()
        .lines()
        .limit(1)
        .findFirst()
        .orElse(EMPTY_LINE);

    if (!expectedLine.equals(actualLine)) {
      failWithMessage(INVALID_HEADER + NOT_EQUAL_MSG_TEMPLATE, expectedLine, actualLine);
    }

    return this;
  }

  ReportAssert hasValues(int lineNumber, String... values) {
    isNotNull();

    long lineCount = actual.getReportContent().lines().count();
    if (lineNumber + 1 >= lineCount)
      failWithMessage(INVALID_LINE_NUMBER, lineNumber, lineCount - 1);

    String expectedLine = asCsvLine(values);
    String actualLine = actual.getReportContent()
        .lines()
        .skip(lineNumber + 1)
        .limit(1)
        .findFirst()
        .orElse(EMPTY_LINE);

    if (!expectedLine.equals(actualLine)) {
      failWithMessage(INVALID_DATA + NOT_EQUAL_MSG_TEMPLATE,
          lineNumber + 1, expectedLine, actualLine);
    }

    return this;
  }

  private String asCsvLine(String... elements) {
    return String.join(",", elements);
  }
}
