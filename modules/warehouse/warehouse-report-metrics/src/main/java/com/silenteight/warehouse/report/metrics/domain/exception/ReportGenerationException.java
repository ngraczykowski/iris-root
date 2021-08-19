package com.silenteight.warehouse.report.metrics.domain.exception;

import static java.lang.String.format;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = -6982243211028959671L;

  public ReportGenerationException(long id, RuntimeException e) {
    super(format("Cannot generate report with id=%d", id), e);
  }
}
