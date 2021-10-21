package com.silenteight.warehouse.report.rbs.domain.exception;

import static java.lang.String.format;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 6036336163286521111L;

  public ReportNotFoundException(long id) {
    super(format("Rb Scorer report with id=%d not found", id));
  }
}
