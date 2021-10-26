package com.silenteight.warehouse.report.accuracy.v1.domain.exception;

import static java.lang.String.format;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = -5105914434036439453L;

  public ReportGenerationException(long id, RuntimeException e) {
    super(format("Cannot generate accuracy report with id=%d", id), e);
  }
}
