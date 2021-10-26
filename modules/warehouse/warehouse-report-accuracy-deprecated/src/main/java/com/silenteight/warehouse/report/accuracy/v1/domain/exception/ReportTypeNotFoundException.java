package com.silenteight.warehouse.report.accuracy.v1.domain.exception;

import static java.lang.String.format;

public class ReportTypeNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 6170149652000772886L;

  public ReportTypeNotFoundException(String id) {
    super(format("Accuracy report type with id=%s not found", id));
  }
}
