package com.silenteight.warehouse.report.metrics.domain.exception;

import static java.lang.String.format;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = -4784808998541152751L;

  public ReportGenerationException(long id, RuntimeException e) {
    super(format("Cannot generate Metrics report with id=%d", id), e);
  }
}
