package com.silenteight.warehouse.report.metrics.domain.exception;

import static java.lang.String.format;

public class ReportTypeNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5853787670519366869L;

  public ReportTypeNotFoundException(String id) {
    super(format("Report type with id=%s not found", id));
  }
}
