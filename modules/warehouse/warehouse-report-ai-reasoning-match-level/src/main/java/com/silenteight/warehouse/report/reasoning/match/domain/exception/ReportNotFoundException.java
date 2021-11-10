package com.silenteight.warehouse.report.reasoning.match.domain.exception;

import static java.lang.String.format;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4495924664801288664L;

  public ReportNotFoundException(long id) {
    super(format("AI Reasoning Match Level report with id=%d not found", id));
  }
}
