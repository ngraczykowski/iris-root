package com.silenteight.connector.ftcc.ingest.state.exception;

import static java.lang.String.format;

public class UnknownAlertStateException extends IllegalStateException {

  public UnknownAlertStateException(String status) {
    super(format("Unknown alert state for status: %s.", status));
  }
}
