package com.silenteight.sens.webapp.backend.report.exception;

public class ReportNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5845213713220897556L;

  public ReportNotFoundException(String msg) {
    super(msg);
  }
}
