package com.silenteight.connector.ftcc.ingest.state.exception;

import static java.lang.String.format;

public class UnknownAlertStateException extends IllegalStateException {

  private static final long serialVersionUID = -628479022574658222L;

  public UnknownAlertStateException(String status) {
    super(format("Unknown alert state for status: %s.", status));
  }
}
