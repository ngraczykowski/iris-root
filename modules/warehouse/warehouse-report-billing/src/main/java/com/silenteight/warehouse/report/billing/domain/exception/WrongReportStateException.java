package com.silenteight.warehouse.report.billing.domain.exception;

import com.silenteight.warehouse.report.billing.domain.ReportState;

import static java.lang.String.format;

public class WrongReportStateException extends RuntimeException {

  private static final long serialVersionUID = -4230235587062499808L;

  public WrongReportStateException(Long id, ReportState actual, ReportState desirable) {
    super(format("Cannot change state from %s to %s for Billing Report with id: %d",
        actual, desirable, id));
  }
}
