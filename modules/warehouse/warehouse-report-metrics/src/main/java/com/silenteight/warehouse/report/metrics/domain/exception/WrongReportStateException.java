package com.silenteight.warehouse.report.metrics.domain.exception;

import com.silenteight.warehouse.report.metrics.domain.ReportState;

import static java.lang.String.format;

public class WrongReportStateException extends RuntimeException {

  private static final long serialVersionUID = -3729478842682162958L;

  public WrongReportStateException(Long id, ReportState actual, ReportState desirable) {
    super(format("Cannot change state from %s to %s for Metrics Report with id: %d",
        actual, desirable, id));
  }
}
