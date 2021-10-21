package com.silenteight.warehouse.report.rbs.domain.exception;

import com.silenteight.warehouse.report.rbs.domain.ReportState;

import static java.lang.String.format;

public class WrongReportStateException extends RuntimeException {

  private static final long serialVersionUID = 5250346123141918435L;

  public WrongReportStateException(Long id, ReportState actual, ReportState desirable) {
    super(format("Cannot change state from %s to %s for RB Scorer Report with id: %d",
        actual, desirable, id));
  }
}
