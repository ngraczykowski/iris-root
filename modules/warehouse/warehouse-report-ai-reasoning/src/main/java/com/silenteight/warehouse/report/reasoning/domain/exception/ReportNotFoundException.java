package com.silenteight.warehouse.report.reasoning.domain.exception;

import static java.lang.String.format;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4495924664801288664L;

  public ReportNotFoundException(String id) {
    super(format("AI reasoning report with id=%s not found", id));
  }
}
