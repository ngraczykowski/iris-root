package com.silenteight.warehouse.report.rbs.domain.exception;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = 37947672705178606L;

  public ReportGenerationException(RuntimeException e) {
    super(e);
  }
}
