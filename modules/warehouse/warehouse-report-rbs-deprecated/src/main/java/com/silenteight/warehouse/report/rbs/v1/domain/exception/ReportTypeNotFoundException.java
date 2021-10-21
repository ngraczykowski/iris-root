package com.silenteight.warehouse.report.rbs.v1.domain.exception;

import static java.lang.String.format;

public class ReportTypeNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -2637947674405178607L;

  public ReportTypeNotFoundException(String id) {
    super(format("Report type with id=%s not found", id));
  }
}
