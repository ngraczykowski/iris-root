package com.silenteight.warehouse.report.persistence;

import static java.lang.String.format;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -8976125971029867670L;

  public ReportNotFoundException(long id) {
    super(format("Report with id=%s not found", id));
  }
}
