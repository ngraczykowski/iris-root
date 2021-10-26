package com.silenteight.warehouse.report.accuracy.domain.exception;

import static java.lang.String.format;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -8976145977029847670L;

  public ReportNotFoundException(long id) {
    super(format("Accuracy report with id=%s not found", id));
  }
}
