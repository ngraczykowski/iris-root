package com.silenteight.warehouse.report.accuracy.domain.exception;

import com.silenteight.warehouse.report.accuracy.domain.ReportState;

import static java.lang.String.format;

public class WrongReportStateException extends RuntimeException {

  private static final long serialVersionUID = 8181352215226571509L;

  public WrongReportStateException(long id, ReportState actual, ReportState desirable) {
    super(format("Cannot change state from %s to %s for Accuracy Report with id: %s",
        actual, desirable, id));
  }
}
