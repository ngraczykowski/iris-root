package com.silenteight.warehouse.report.reasoning.domain.exception;

import com.silenteight.warehouse.report.reasoning.domain.ReportState;

import static java.lang.String.format;

public class WrongReportStateException extends RuntimeException {

  private static final long serialVersionUID = -6919527131569831236L;

  public WrongReportStateException(Long id, ReportState actual, ReportState desirable) {
    super(format("Cannot change state from %s to %s for AI Reasoning Report with id: %s",
        actual, desirable, id));
  }
}
