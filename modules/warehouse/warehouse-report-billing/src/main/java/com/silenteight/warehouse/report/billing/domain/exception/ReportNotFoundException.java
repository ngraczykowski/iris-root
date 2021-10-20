package com.silenteight.warehouse.report.billing.domain.exception;

import static java.lang.String.format;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 2321635976582543739L;

  public ReportNotFoundException(long id) {
    super(format("Billing report with id=%s not found", id));
  }
}
