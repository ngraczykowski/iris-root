package com.silenteight.warehouse.report.reasoning.v1.domain.exception;

import com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedReportState;

import static java.lang.String.format;

public class WrongReportStateException extends RuntimeException {

  private static final long serialVersionUID = -6919527131569831236L;

  public WrongReportStateException(
      Long id, DeprecatedReportState actual, DeprecatedReportState desirable) {

    super(format("Cannot change state from %s to %s for AI Reasoning Report with id: %s",
        actual, desirable, id));
  }
}
