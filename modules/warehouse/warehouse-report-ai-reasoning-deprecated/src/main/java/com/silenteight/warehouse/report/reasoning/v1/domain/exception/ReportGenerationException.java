package com.silenteight.warehouse.report.reasoning.v1.domain.exception;

import static java.lang.String.format;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = 3674331329957236162L;

  public ReportGenerationException(long id, RuntimeException e) {
    super(format("Cannot generate AI Reasoning report with id=%d", id), e);
  }
}
