package com.silenteight.warehouse.indexer.query.streaming.exception;

import static java.lang.String.join;

public class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = -821608885611168291L;

  public ReportGenerationException(String reportName, String message, Throwable cause) {
    super(join(": ", reportName, message), cause);
  }
}
