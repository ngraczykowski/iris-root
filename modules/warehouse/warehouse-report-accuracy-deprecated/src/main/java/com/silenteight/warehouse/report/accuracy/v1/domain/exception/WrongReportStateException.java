package com.silenteight.warehouse.report.accuracy.v1.domain.exception;

import com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedReportState;

import static java.lang.String.format;

public class WrongReportStateException extends RuntimeException {

  private static final long serialVersionUID = 8181352215226571509L;

  public WrongReportStateException(
      Long id, DeprecatedReportState actual, DeprecatedReportState desirable) {

    super(format("Cannot change state from %s to %s for Accuracy Report with id: %s",
        actual, desirable, id));
  }
}
