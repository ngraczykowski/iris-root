package com.silenteight.warehouse.report.metrics.domain.exception;

import static java.lang.String.format;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7890334103241782489L;

  public ReportNotFoundException(long id) {
    super(format("Metrics report with id=%d not found", id));
  }
}
