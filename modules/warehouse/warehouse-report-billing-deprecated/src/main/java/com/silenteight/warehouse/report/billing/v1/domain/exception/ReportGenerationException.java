package com.silenteight.warehouse.report.billing.v1.domain.exception;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = 5048778096930455712L;

  public ReportGenerationException(RuntimeException e) {
    super(e);
  }
}
