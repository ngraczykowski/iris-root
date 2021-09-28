package com.silenteight.warehouse.indexer.query.streaming;

import static java.lang.String.join;

class ReportGenerationException extends RuntimeException {

  private static final long serialVersionUID = -821608885611168291L;

  ReportGenerationException(String reportName, String message, Throwable cause) {
    super(join(": ", reportName, message), cause);
  }
}
