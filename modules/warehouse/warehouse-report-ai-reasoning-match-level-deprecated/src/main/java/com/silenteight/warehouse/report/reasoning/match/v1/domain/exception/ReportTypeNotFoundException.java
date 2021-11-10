package com.silenteight.warehouse.report.reasoning.match.v1.domain.exception;

import static java.lang.String.format;

public class ReportTypeNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5614306211324036321L;

  public ReportTypeNotFoundException(String id) {
    super(format("AI Reasoning Match Level report type with id=%s not found", id));
  }
}
