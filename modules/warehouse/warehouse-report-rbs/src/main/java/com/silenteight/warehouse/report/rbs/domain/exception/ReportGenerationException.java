package com.silenteight.warehouse.report.rbs.domain.exception;

import static java.lang.String.format;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = -6516230865962429657L;

  public ReportGenerationException(long id, RuntimeException e) {
    super(format("Cannot generate RB Scorer report with id=%d",id),e);
  }
}
